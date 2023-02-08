package br.com.fastfood.restaurant.repository;

import br.com.fastfood.restaurant.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.UUID;

public interface CustomerRepository extends
        CrudRepository<Customer, UUID>,
        ListPagingAndSortingRepository<Customer, UUID> {
}
