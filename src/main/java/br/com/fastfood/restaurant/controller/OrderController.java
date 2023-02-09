package br.com.fastfood.restaurant.controller;

import br.com.fastfood.restaurant.entity.*;
import br.com.fastfood.restaurant.event.OrderPlacedEvent;
import br.com.fastfood.restaurant.repository.CustomerRepository;
import br.com.fastfood.restaurant.repository.MenuRepository;
import br.com.fastfood.restaurant.repository.OrderRepository;
import br.com.fastfood.restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    private final ApplicationEventPublisher publisher;

    @Autowired
    public OrderController(
            OrderRepository orderRepository,
            RestaurantRepository restaurantRepository,
            MenuRepository menuRepository,
            CustomerRepository customerRepository,
            ApplicationEventPublisher publisher
    ) {
        this.publisher = publisher;
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    @Transactional
    public Order post(@RequestBody @Valid br.com.fastfood.restaurant.dto.Order orderData) {
        Order order = new Order();
        order.setDiscount(orderData.discount());
        order.setPaymentMethod(orderData.paymentMethod());

        Restaurant restaurant = this.restaurantRepository.findById(orderData.restaurantId()).get();
        order.setRestaurant(restaurant);

        Customer customer = this.customerRepository.findById(orderData.customerId()).get();
        order.setCustomer(customer);

        for (br.com.fastfood.restaurant.dto.OrderItem item : orderData.items()) {
            Menu menu = this.menuRepository.findByIdAndRestaurant(item.menuId(), restaurant).orElseThrow();
            OrderItem orderItem = OrderItem.create(menu.getName(), menu.getPrice(), item.qty());
            order.addItem(orderItem);
        }

        this.publisher.publishEvent(new OrderPlacedEvent(order));
        return this.orderRepository.save(order);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable UUID id) {
        Order order = this.orderRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        order.setStatus(Order.Status.Canceled);
        this.orderRepository.save(order);
    }

    @GetMapping
    public Page<Order> list(
            @RequestParam(name = "per_page", defaultValue = "50") int perPage,
            @RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
            @RequestParam(name = "sort_dir", defaultValue = "asc") String sortDirection
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable page = PageRequest.ofSize(perPage).withSort(sort);
        return this.orderRepository.findAll(page);
    }
}
