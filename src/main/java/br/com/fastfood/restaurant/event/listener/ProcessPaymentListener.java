package br.com.fastfood.restaurant.event.listener;

import br.com.fastfood.restaurant.entity.Order;
import br.com.fastfood.restaurant.event.OrderPlacedEvent;
import br.com.fastfood.restaurant.payment.InvalidPaymentMethodException;
import br.com.fastfood.restaurant.payment.PaymentMethod;
import br.com.fastfood.restaurant.payment.PaymentMethodBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ProcessPaymentListener implements ApplicationListener<OrderPlacedEvent> {
    @Override
    public void onApplicationEvent(OrderPlacedEvent event) {
        Order order = event.getOrder();

        try {
            PaymentMethod method = PaymentMethodBuilder.get(order.getPaymentMethod());
            method.processPayment(order);
        } catch (InvalidPaymentMethodException e) {
            System.out.println("Payment method '" + order.getPaymentMethod() + "' not found");
        }
    }
}
