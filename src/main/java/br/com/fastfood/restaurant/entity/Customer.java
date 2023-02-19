package br.com.fastfood.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

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

    @NotEmpty
    private String password;

    private List<String> authorities;

    @Valid
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Address billingAddress;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    private Address deliveryAddress;

    public Customer() {
        this.authorities = new ArrayList<>();
        this.authorities.add("ROLE_CUSTOMER");
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Address getBillingAddress() {
        return this.billingAddress;
    }

    public void setBillingAddress(Address address) {
        this.billingAddress = address;
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

    public void setDeliveryAddress(Address address) {
        this.deliveryAddress = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String authority : this.authorities) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }
}
