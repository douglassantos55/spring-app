package br.com.fastfood.restaurant.controller;

import br.com.fastfood.restaurant.entity.Customer;
import br.com.fastfood.restaurant.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/customers")
public class CustomerController {
    private final CustomerRepository repository;

    @Autowired
    public CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Customer post(@RequestBody @Valid Customer customer) {
        return this.repository.save(customer);
    }
}
