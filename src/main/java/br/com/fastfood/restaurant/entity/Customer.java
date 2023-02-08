package br.com.fastfood.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty
    private String name;

    @Email
    private String email;

    @Valid
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Address billingAddress;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    private Address deliveryAddress;

    public Address getBillingAddress() {
        return this.billingAddress;
    }

    /**
     * Returns customer's delivery address.
     * Defaults to billing address if not specified
     *
     * @return Address
     */
    public Address getDeliveryAddress() {
        if (this.deliveryAddress == null) {
            return this.getBillingAddress();
        }
        return this.deliveryAddress;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
