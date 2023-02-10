package br.com.fastfood.restaurant;

import br.com.fastfood.restaurant.entity.OrderItem;
import br.com.fastfood.restaurant.service.delivery.GrpcDeliveryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class GrpcDeliveryTests {
    private final GrpcDeliveryService service;

    @Autowired
    public GrpcDeliveryTests(GrpcDeliveryService service) {
        this.service = service;
    }

    @Test
    public void getQuote() {
        List<OrderItem> items = new ArrayList<>();
        items.add(OrderItem.create("bread", 500, 100));
        int quote = this.service.getQuote("Campinas, São Paulo, SP", "Santos, São Paulo, SP", items);
        assertNotEquals("should get a quote", 0, quote);
    }
}
