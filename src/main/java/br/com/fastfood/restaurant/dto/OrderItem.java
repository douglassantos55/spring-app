package br.com.fastfood.restaurant.dto;

import br.com.fastfood.restaurant.entity.Menu;
import br.com.fastfood.restaurant.validation.constraint.ExistsConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItem(
        @NotNull
        @ExistsConstraint(entityTarget = Menu.class)
        UUID menuId,

        @Min(1)
        int qty
) {
}
