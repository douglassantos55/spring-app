package br.com.fastfood.restaurant;

import br.com.fastfood.restaurant.controller.CustomerController;
import br.com.fastfood.restaurant.entity.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

@SpringBootTest
class RestaurantApplicationTests {
	@Autowired
	private CustomerController customerController;

	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.customerController).build();
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
		MvcResult result = this.mockMvc.perform(
				MockMvcRequestBuilders.post("/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"name\":\"John doe\",\"email\":\"johndoe@email.com\",\"billingAddress\":{\"zipcode\":\"153533\",\"street\":\"Avenue\",\"number\":\"51353\"}}")
		).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

		ObjectMapper mapper = new ObjectMapper();
		Customer customer = mapper.readValue(result.getResponse().getContentAsString(), Customer.class);

		this.mockMvc.perform(MockMvcRequestBuilders.delete("/customers/" + customer.getId()))
				.andExpect(MockMvcResultMatchers.status().isNoContent());
	}
}
