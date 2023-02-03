package br.com.fastfood.restaurant.repository;

import br.com.fastfood.restaurant.entity.Menu;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MenuRepository extends CrudRepository<Menu, UUID> {

}
