package br.com.fastfood.restaurant.dto;

import br.com.fastfood.restaurant.entity.Menu;
import br.com.fastfood.restaurant.validation.constraint.ExistsConstraint;
import br.com.fastfood.restaurant.validation.constraint.LessThanEquals;
import br.com.fastfood.restaurant.validation.group.Dependant;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@GroupSequence({ OrderItem.class, Dependant.class })
@LessThanEquals(entity = Menu.class, id="menuId", field = "qty", source = "getStock", groups = Dependant.class)
public record OrderItem(
        @NotNull
        UUID menuId,

        @Min(1)
        int qty
) {
}
