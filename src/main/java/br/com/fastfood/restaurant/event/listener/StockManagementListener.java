package br.com.fastfood.restaurant.event.listener;

import br.com.fastfood.restaurant.entity.Menu;
import br.com.fastfood.restaurant.entity.Order;
import br.com.fastfood.restaurant.entity.OrderItem;
import br.com.fastfood.restaurant.event.OrderCanceledEvent;
import br.com.fastfood.restaurant.event.OrderPlacedEvent;
import br.com.fastfood.restaurant.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockManagementListener {
    private final MenuRepository repository;

    @Autowired
    public StockManagementListener(MenuRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void reduceStock(OrderPlacedEvent event) {
        Order order = event.getOrder();
        List<Menu> menu = order.getRestaurant().getMenu();

        for (OrderItem item : order.getItems()) {
            for (Menu menuItem : menu) {
                if (item.getName().equals(menuItem.getName())) {
                    menuItem.reduceStock(item.getQty());
                    this.repository.save(menuItem);
                }
            }
        }
    }

    @EventListener
    public void restoreStock(OrderCanceledEvent event) {
        Order order = (Order) event.getSource();
        List<Menu> menu = order.getRestaurant().getMenu();

        for (OrderItem item : order.getItems()) {
            for (Menu menuItem : menu) {
                if (item.getName().equals(menuItem.getName())) {
                    menuItem.restoreStock(item.getQty());
                    this.repository.save(menuItem);
                }
            }
        }
    }
}
