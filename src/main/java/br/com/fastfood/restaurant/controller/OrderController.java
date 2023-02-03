package br.com.fastfood.restaurant.controller;

import br.com.fastfood.restaurant.entity.OrderItem;
import br.com.fastfood.restaurant.entity.Menu;
import br.com.fastfood.restaurant.entity.Order;
import br.com.fastfood.restaurant.repository.MenuRepository;
import br.com.fastfood.restaurant.repository.OrderRepository;
import br.com.fastfood.restaurant.repository.RestaurantRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public OrderController(
            OrderRepository orderRepository,
            RestaurantRepository restaurantRepository,
            MenuRepository menuRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @PostMapping
    public Order post(@RequestBody @Valid br.com.fastfood.restaurant.dto.Order orderData) {
        Order order = new Order();
        order.setItems(new ArrayList<>());
        order.setDiscount(orderData.discount());
        order.setRestaurant(this.restaurantRepository.findById(orderData.restaurantId()).get());

        for (br.com.fastfood.restaurant.dto.OrderItem item : orderData.items()) {
            Menu menu = this.menuRepository.findById(item.menuId()).get();
            OrderItem orderItem = OrderItem.create(menu.getName(), menu.getPrice(), item.qty());
            order.addItem(orderItem);
        }

        return this.orderRepository.save(order);
    }
}
