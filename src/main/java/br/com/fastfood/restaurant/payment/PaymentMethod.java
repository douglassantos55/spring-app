package br.com.fastfood.restaurant.payment;

import br.com.fastfood.restaurant.entity.Order;

public interface PaymentMethod {
    /**
     * Process payment for the given order.
     * Implementations may mutate the order's state.
     *
     * @param order
     */
    public void processPayment(Order order);
}
