package br.com.fastfood.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    public enum Status {
        Pending,
        Paid,
        Canceled,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Min(0)
    private int discount;

    @NotNull
    @JsonIgnoreProperties({ "menu" })
    @ManyToOne(targetEntity = Restaurant.class, cascade = CascadeType.ALL)
    private Restaurant restaurant;

    @NotEmpty
    @Valid
    @OneToMany(targetEntity = OrderItem.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    private Status status;

    public Order() {
        this.status = Status.Pending;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getTotal() {
        int total = -this.discount;
        for (OrderItem item : this.getItems()) {
            total += item.getSubtotal();
        }
        return total;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }
}