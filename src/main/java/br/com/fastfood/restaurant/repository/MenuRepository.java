package br.com.fastfood.restaurant.repository;

import br.com.fastfood.restaurant.entity.Menu;
import br.com.fastfood.restaurant.entity.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends CrudRepository<Menu, UUID> {
    Optional<Menu> findByIdAndRestaurant(UUID id, Restaurant restaurant);
}
