package br.com.fastfood.restaurant.models;

import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface RestaurantRepository extends ListCrudRepository<Restaurant, UUID> {
}
