package br.com.fastfood.restaurant.event;

import br.com.fastfood.restaurant.entity.Order;
import org.springframework.context.ApplicationEvent;

public class OrderPlacedEvent extends ApplicationEvent {
    private final Order order;

    public OrderPlacedEvent(Order order) {
        super(order);
        this.order = order;
    }

    public Order getOrder() {
        return this.order;
    }
}
