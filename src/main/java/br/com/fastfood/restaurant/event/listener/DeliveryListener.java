package br.com.fastfood.restaurant.event.listener;

import br.com.fastfood.restaurant.entity.Address;
import br.com.fastfood.restaurant.entity.Order;
import br.com.fastfood.restaurant.event.OrderPlacedEvent;
import br.com.fastfood.restaurant.service.delivery.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryListener implements ApplicationListener<OrderPlacedEvent> {
    @Value("${delivery.origin}")
    private String origin;

    private final DeliveryService service;

    @Autowired
    public DeliveryListener(DeliveryService service) {
        this.service = service;
    }

    @Override
    public void onApplicationEvent(OrderPlacedEvent event) {
        Order order = event.getOrder();
        Address deliveryAddress = order.getCustomer().getDeliveryAddress();
        int quote = this.service.getQuote(this.origin, deliveryAddress.toString(), order.getItems());
        order.setDeliveryValue(quote);
    }
}
