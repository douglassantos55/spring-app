package br.com.fastfood.restaurant.event;

import br.com.fastfood.restaurant.entity.Order;
import org.springframework.context.ApplicationEvent;

public class OrderCanceledEvent extends ApplicationEvent {
    public OrderCanceledEvent(Order order) {
        super(order);
    }
}
