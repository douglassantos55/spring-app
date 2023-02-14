package br.com.fastfood.restaurant;

import br.com.fastfood.restaurant.entity.*;
import br.com.fastfood.restaurant.repository.CustomerRepository;
import br.com.fastfood.restaurant.repository.MenuRepository;
import br.com.fastfood.restaurant.repository.OrderRepository;
import br.com.fastfood.restaurant.repository.RestaurantRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@ContextConfiguration(classes = Config.class)
public class OrderTests {
    private MockMvc mock;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        this.mock = MockMvcBuilders.webAppContextSetup(context).build();
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setName("john doe");
        customer.setEmail("john@email.com");

        customer.setBillingAddress(new Address("Campinas, São Paulo, SP", "325", "13845-000"));
        return this.customerRepository.save(customer);
    }

    private Restaurant createRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("coffee shop");
        restaurant.setDescription("a nice coffee shop");
        restaurant.setMenu(this.createMenu());
        return this.restaurantRepository.save(restaurant);
    }

    private List<Menu> createMenu() {
        List<Menu> menu = new ArrayList<>();
        menu.add(new Menu("coffee", 750, 100));
        menu.add(new Menu("cake", 3750, 10));
        return menu;
    }

    @Test
    public void createOrderInvalidCustomer() throws Exception {
        Restaurant restaurant = this.createRestaurant();
        Menu item = restaurant.getMenu().get(0);

        this.mock.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"" + UUID.randomUUID() + "\",\"restaurantId\":\"" + restaurant.getId() + "\",\"paymentMethod\":\"cash\",\"items\":[{\"qty\":10,\"menuId\":\"" + item.getId() + "\"}],\"discount\":5}")

        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createOrderValidCustomer() throws Exception {
        Customer customer = this.createCustomer();
        Restaurant restaurant = this.createRestaurant();
        Menu item = restaurant.getMenu().get(0);

        MvcResult result = this.mock.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"" + customer.getId() + "\",\"restaurantId\":\"" + restaurant.getId() + "\",\"paymentMethod\":\"cash\",\"items\":[{\"qty\":10,\"menuId\":\"" + item.getId() + "\"}],\"discount\":5}")

        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        // disable so that our totals don't blow everything up
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Order order = mapper.readValue(result.getResponse().getContentAsString(), Order.class);
        assertEquals("should save customer", order.getCustomer().getId(), customer.getId());

    }

    @Test
    public void calculatesDeliveryValue() throws Exception {

        Customer customer = this.createCustomer();
        Restaurant restaurant = this.createRestaurant();
        Menu item = restaurant.getMenu().get(0);

        MvcResult result = this.mock.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"" + customer.getId() + "\",\"restaurantId\":\"" + restaurant.getId() + "\",\"paymentMethod\":\"cash\",\"items\":[{\"qty\":10,\"menuId\":\"" + item.getId() + "\"}],\"discount\":5}")

        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        // disable so that our totals don't blow everything up
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Order order = mapper.readValue(result.getResponse().getContentAsString(), Order.class);
        assertNotEquals("should have delivery value", 0, order.getDeliveryValue());
    }

    @Test
    public void reducesStock() throws Exception {
        Customer customer = this.createCustomer();
        Restaurant restaurant = this.createRestaurant();
        Menu item = restaurant.getMenu().get(0);

        int originalStock = item.getStock();

        MvcResult result = this.mock.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"" + customer.getId() + "\",\"restaurantId\":\"" + restaurant.getId() + "\",\"paymentMethod\":\"cash\",\"items\":[{\"qty\":10,\"menuId\":\"" + item.getId() + "\"}],\"discount\":5}")
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        // reload instance from db
        item = this.menuRepository.findById(item.getId()).get();

        assertEquals("should reduce stock", originalStock-10, item.getStock());
    }

    @Test
    @Transactional
    public void restoreStock() throws Exception {
        Order order = this.orderRepository.findAll().iterator().next();
        OrderItem orderItem = order.getItems().get(0);
        Menu item = order.getRestaurant().getMenu().get(0);

        int original = item.getStock();
        this.mock.perform(MockMvcRequestBuilders.put("/orders/" + order.getId()));

        // reload from db
        item = this.menuRepository.findById(item.getId()).get();

        assertEquals("should restore stock", original + orderItem.getQty(), item.getStock());
    }

    @Test
    @Transactional
    public void sendOrderCanceledEmail() throws Exception {
        Order order = new Order();
        order.setCustomer(this.createCustomer());
        order.setRestaurant(this.createRestaurant());

        this.orderRepository.save(order);

        this.mock.perform(MockMvcRequestBuilders.put("/orders/" + order.getId()));
        JavaMailSender sender = this.mock.getDispatcherServlet().getWebApplicationContext().getBean(JavaMailSender.class);
        verify(sender).send(sender.createMimeMessage());
    }

    @Test
    public void sendOrderPlacedEmail() throws Exception {
        Customer customer = this.createCustomer();
        Restaurant restaurant = this.createRestaurant();
        Menu item = restaurant.getMenu().get(0);

        MvcResult result = this.mock.perform(
                MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":\"" + customer.getId() + "\",\"restaurantId\":\"" + restaurant.getId() + "\",\"paymentMethod\":\"cash\",\"items\":[{\"qty\":10,\"menuId\":\"" + item.getId() + "\"}],\"discount\":5}")
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        JavaMailSender sender = this.mock.getDispatcherServlet().getWebApplicationContext().getBean(JavaMailSender.class);
        verify(sender).send(sender.createMimeMessage());
    }
}