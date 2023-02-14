package br.com.fastfood.restaurant;

import br.com.fastfood.restaurant.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

@SpringBootTest
@ContextConfiguration(classes = Config.class)
public class CustomerTests {
    private MockMvc mockMvc;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void createCustomerInvalidData() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"emailatgmail.com\"}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createCustomerNoBillingAddress() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"john doe\",\"email\":\"email@gmail.com\"}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createCustomerInvalidBillingData() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"john doe\",\"email\":\"email@gmail.com\",\"billingAddress\":{\"street\":\"\",\"number\":\"\",\"zipcode\":\"\"}}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createCustomerInvalidZipcode() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"john doe\",\"email\":\"email@gmail.com\",\"billingAddress\":{\"street\":\"rua abc\",\"number\":\"155\",\"zipcode\":\"00000000\"}}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createValidCustomer() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"john doe\",\"email\":\"email@gmail.com\",\"billingAddress\":{\"street\":\"Avenue\",\"number\":\"5153\",\"zipcode\":\"01001000\"}}")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateNonExistingCustomer() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"john doe\",\"email\":\"email@gmail.com\",\"billingAddress\":{\"street\":\"Avenue\",\"number\":\"5153\",\"zipcode\":\"01001000\"}}")
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateInvalidId() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/somethhingotherthanuuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"john doe\",\"email\":\"email@gmail.com\",\"billingAddress\":{\"street\":\"Avenue\",\"number\":\"5153\",\"zipcode\":\"01001000\"}}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateInvalidData() throws Exception {
        Customer customer = this.createCustomer();

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"emailatgmaildotcom\",\"billingAddress\":{\"street\":\"\",\"number\":\"\",\"zipcode\":\"\"}}")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void updateValidData() throws Exception {
        Customer customer = this.createCustomer();

        this.mockMvc.perform(
                MockMvcRequestBuilders.put("/customers/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"james smith\",\"email\":\"james@email.com\",\"billingAddress\":{\"street\":\"9th avenue\",\"number\":\"325\",\"zipcode\":\"01001000\"}}")
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"id\":\"" + customer.getId() + "\",\"name\":\"james smith\",\"email\":\"james@email.com\",\"billingAddress\":{\"street\":\"9th avenue\",\"number\":\"325\",\"zipcode\":\"01001000\"}}"));
    }

    @Test
    void deleteNonExistingCustomer() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/customers/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteInvalidUUID() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/customers/somethingotherthanuuid"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deleteExistingCustomer() throws Exception {
        Customer customer = this.createCustomer();
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/customers/" + customer.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private Customer createCustomer() throws Exception {
        MvcResult result = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John doe\",\"email\":\"johndoe@email.com\",\"billingAddress\":{\"zipcode\":\"01001000\",\"street\":\"Avenue\",\"number\":\"51353\"}}")
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result.getResponse().getContentAsString(), Customer.class);
    }
}
