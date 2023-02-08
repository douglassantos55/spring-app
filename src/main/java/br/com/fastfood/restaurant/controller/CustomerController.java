package br.com.fastfood.restaurant.controller;

import br.com.fastfood.restaurant.entity.Customer;
import br.com.fastfood.restaurant.repository.CustomerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

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

    @PutMapping("/{id}")
    public Customer put(@PathVariable UUID id, @RequestBody @Valid Customer data) {
        Customer customer = this.repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        customer.setEmail(data.getEmail());
        customer.setName(data.getName());
        customer.setBillingAddress(data.getBillingAddress());
        customer.setDeliveryAddress(data.getDeliveryAddress());

        return this.repository.save(customer);
    }

    @GetMapping
    public Page<Customer> list(
            @Min(1) @RequestParam(value = "page", defaultValue = "1") int page,
            @Min(1) @RequestParam(value = "per_page", defaultValue = "50") int perPage
    ) {
        return this.repository.findAll(PageRequest.of(page-1, perPage));
    }
}
