package br.com.fastfood.restaurant.dto;

import br.com.fastfood.restaurant.entity.Customer;
import br.com.fastfood.restaurant.entity.Restaurant;
import br.com.fastfood.restaurant.validation.OrderSequenceProvider;
import br.com.fastfood.restaurant.validation.constraint.ExistsConstraint;
import br.com.fastfood.restaurant.validation.group.CreditPayment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.group.GroupSequenceProvider;

import java.util.List;
import java.util.UUID;

@GroupSequenceProvider(OrderSequenceProvider.class)
public record Order(
        @NotNull
        @ExistsConstraint(entityTarget = Restaurant.class)
        UUID restaurantId,

        @NotNull
        @ExistsConstraint(entityTarget = Customer.class)
        UUID customerId,

        @NotEmpty
        String paymentMethod,

        @NotEmpty(groups = CreditPayment.class)
        @CreditCardNumber(groups = CreditPayment.class)
        String ccNumber,

        @NotEmpty(groups = CreditPayment.class)
        String ccName,

        @NotEmpty(groups = CreditPayment.class)
        @Pattern(regexp = "\\d{2}/\\d{4}", message = "Expiration date must be MM/YYYY", groups = CreditPayment.class)
        String ccExpDate,

        @NotNull(groups = CreditPayment.class)
        int ccCvc,

        @Size(min = 1, message = "should have at least {min} item")
        @Valid
        List<OrderItem> items,

        @Min(value = 0)
        int discount

) {
}
