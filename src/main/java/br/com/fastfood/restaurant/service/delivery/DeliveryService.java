package br.com.fastfood.restaurant.service.delivery;

import br.com.fastfood.restaurant.entity.OrderItem;

import java.util.List;

public interface DeliveryService {
    int getQuote(String source, String destination, List<OrderItem> items);
}
