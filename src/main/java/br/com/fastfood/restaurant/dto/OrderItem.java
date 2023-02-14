package br.com.fastfood.restaurant.dto;

import br.com.fastfood.restaurant.entity.Menu;
import br.com.fastfood.restaurant.validation.constraint.LessThanEquals;
import br.com.fastfood.restaurant.validation.group.Dependent;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@GroupSequence({ OrderItem.class, Dependent.class })
@LessThanEquals(entity = Menu.class, id="menuId", field = "qty", source = "getStock", groups = Dependent.class)
public record OrderItem(
        @NotNull
        UUID menuId,

        @Min(1)
        int qty
) {
}
