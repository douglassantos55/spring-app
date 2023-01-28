package br.com.fastfood.restaurant;

import br.com.fastfood.restaurant.models.Restaurant;
import br.com.fastfood.restaurant.models.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class RestaurantApplication {
	private final RestaurantRepository repository;

	@Autowired
	public RestaurantApplication(RestaurantRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello() {
		return "hello, World!";
	}

	@GetMapping("/")
	public List<Restaurant> list() {
		return this.repository.findAll();
	}

	@GetMapping("/create")
	public Restaurant create(@RequestParam(name = "name") String name) {
		Restaurant restaurant = new Restaurant();
		restaurant.name = name;
		return this.repository.save(restaurant);
	}
}
