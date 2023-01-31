package br.com.fastfood.restaurant.repository;

import br.com.fastfood.restaurant.entity.Restaurant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.UUID;

public interface RestaurantRepository extends
        CrudRepository<Restaurant, UUID>,
        ListPagingAndSortingRepository<Restaurant, UUID> {
}
