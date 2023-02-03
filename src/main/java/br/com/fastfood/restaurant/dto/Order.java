package br.com.fastfood.restaurant.dto;

import br.com.fastfood.restaurant.entity.Restaurant;
import br.com.fastfood.restaurant.validation.constraint.ExistsConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record Order(
        @NotNull
        @ExistsConstraint(entityTarget = Restaurant.class)
        UUID restaurantId,

        @Size(min = 1, message = "should have at least {min} item")
        @Valid
        List<OrderItem> items,

        @Min(0)
        int discount
) {
}
