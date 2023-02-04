package br.com.fastfood.restaurant.controller;

import br.com.fastfood.restaurant.entity.Menu;
import br.com.fastfood.restaurant.entity.Restaurant;
import br.com.fastfood.restaurant.repository.RestaurantRepository;
import br.com.fastfood.restaurant.storage.Storage;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@RestController
@RequestMapping(path = "/restaurants")
public class RestaurantController {
    private final RestaurantRepository repository;

    private final Storage storage;

    @Autowired
    public RestaurantController(RestaurantRepository repository, Storage storage) {
        this.repository = repository;
        this.storage = storage;
    }

    @Transactional
    @PostMapping
    public Restaurant post(@Valid Restaurant restaurant, @RequestPart(required = false) MultipartFile logo) throws IOException {
        if (logo != null) {
            restaurant.setLogo(this.storage.store(logo, Restaurant.UPLOAD_PATH));
        }

        for (Menu item : restaurant.getMenu()) {
            item.setRestaurant(restaurant);

            if (item.getImage() != null) {
                item.setImgPath(this.storage.store(item.getImage(), Menu.UPLOAD_PATH));
            }
        }

        return this.repository.save(restaurant);
    }

    @GetMapping(path = "/{id}")
    public Restaurant get(@PathVariable UUID id) {
        return this.repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    @Transactional
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) throws IOException {
        Restaurant restaurant = this.repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if (restaurant.getLogo() != null) {
            this.storage.delete(Path.of(restaurant.getLogo()));
        }

        for (Menu item : restaurant.getMenu()) {
            if (item.getImgPath() != null) {
                this.storage.delete(Path.of(item.getImgPath()));
            }
        }

        this.repository.deleteById(id);
    }

    @Transactional
    @PutMapping(path = "/{id}")
    public Restaurant put(@PathVariable UUID id, @Valid Restaurant data, @RequestPart(required = false) MultipartFile logo) throws IOException {
        Restaurant restaurant = this.repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        if (logo != null) {
            if (restaurant.getLogo() != null) {
                this.storage.delete(Path.of(restaurant.getLogo()));
            }
            restaurant.setLogo(this.storage.store(logo, Restaurant.UPLOAD_PATH));
        }

        for (int i = 0; i < restaurant.getMenu().size(); i++) {
            Menu item = restaurant.getMenu().get(i);
            Menu newItem = data.getMenu().get(i);

            if (newItem == null || newItem.getImage() != null && item.getImgPath() != null) {
                this.storage.delete(Path.of(item.getImgPath()));
            } else {
                newItem.setImgPath(item.getImgPath());
            }
        }

        for (Menu item : data.getMenu()) {
            if (item.getImage() != null) {
                item.setImgPath(this.storage.store(item.getImage(), Menu.UPLOAD_PATH));
            }
            item.setRestaurant(restaurant);
        }

        restaurant.setName(data.getName());
        restaurant.setMenu(data.getMenu());
        restaurant.setDescription(data.getDescription());

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
