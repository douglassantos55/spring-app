package br.com.fastfood.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne(targetEntity = Order.class)
    private Order order;

    @NotNull
    @NotEmpty
    private String name;

    private int price;

    @Min(1)
    private int qty;

    public OrderItem() {
    }

    public static OrderItem create(String name, int price, int qty) {
        return new OrderItem(name, price, qty);
    }

    private OrderItem(String name, int price, int qty) {
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    @JsonProperty
    public int getSubtotal() {
        return this.getPrice() * this.getQty();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
