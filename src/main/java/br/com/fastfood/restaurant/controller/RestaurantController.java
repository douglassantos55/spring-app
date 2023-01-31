package br.com.fastfood.restaurant.controller;

import br.com.fastfood.restaurant.entity.Restaurant;
import br.com.fastfood.restaurant.repository.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/restaurants")
public class RestaurantController {
    private final RestaurantRepository repository;

    @Autowired
    public RestaurantController(RestaurantRepository repository)
    {
        this.repository = repository;
    }

    @PostMapping
    public Restaurant post(@RequestBody @Valid Restaurant restaurant)
    {
        return this.repository.save(restaurant);
    }

    @PutMapping(path = "/{id}")
    public Restaurant put(@PathVariable UUID id, @RequestBody @Valid Restaurant restaurant)
    {
        Restaurant item = this.repository.findById(id).orElseThrow();
        restaurant.setId(item.getId());
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
