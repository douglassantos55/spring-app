package br.com.fastfood.restaurant.payment;

import br.com.fastfood.restaurant.entity.Order;

public class CreditCardPaymentMethod implements PaymentMethod {
    @Override
    public void processPayment(Order order) {
        order.setStatus(Order.Status.Paid);
    }
}
