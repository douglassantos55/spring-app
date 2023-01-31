package br.com.fastfood.restaurant.controller;

import br.com.fastfood.restaurant.entity.Restaurant;
import br.com.fastfood.restaurant.repository.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping(path = "/restaurants")
public class RestaurantController {
    private final RestaurantRepository repository;

    @Autowired
    public RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Restaurant post(@RequestBody @Valid Restaurant restaurant) {
        return this.repository.save(restaurant);
    }

    @GetMapping(path = "/{id}")
    public Restaurant get(@PathVariable UUID id) {
        return this.repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable UUID id) {
        if (!this.repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        this.repository.deleteById(id);
    }

    @PutMapping(path = "/{id}")
    public Restaurant put(@PathVariable UUID id, @RequestBody @Valid Restaurant data) {
        Restaurant restaurant = this.repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        restaurant.setName(data.getName());
        restaurant.setDescription(data.getDescription());
        restaurant.setMenu(data.getMenu());

        return this.repository.save(restaurant);
    }

    @GetMapping
    public Page<Restaurant> list(
            @RequestParam(name = "per_page", defaultValue = "50") int perPage,
            @RequestParam(name = "sort_by", defaultValue = "name") String sortBy,
            @RequestParam(name = "sort_dir", defaultValue = "asc") String sortDirection
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable page = PageRequest.ofSize(perPage).withSort(sort);
        return this.repository.findAll(page);
    }
}
