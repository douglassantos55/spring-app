package br.com.fastfood.restaurant.validation;

import br.com.fastfood.restaurant.dto.Order;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;

public class OrderSequenceProvider implements DefaultGroupSequenceProvider<Order> {
    @Override
    public List<Class<?>> getValidationGroups(Order order) {
        List<Class<?>> groups = new ArrayList<>();
        groups.add(Order.class);

        if (order != null && "cc".equals(order.paymentMethod())) {
            groups.add(CreditPayment.class);
        }

        return groups;
    }
}
