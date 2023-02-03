package br.com.fastfood.restaurant.repository;

import br.com.fastfood.restaurant.entity.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.UUID;

public interface OrderRepository extends
        CrudRepository<Order, UUID>,
        ListPagingAndSortingRepository<Order, UUID> {
}
