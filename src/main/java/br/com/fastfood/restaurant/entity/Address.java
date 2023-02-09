package br.com.fastfood.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty
    private String street;

    @NotEmpty
    private String number;

    @NotEmpty
    private String zipcode;

    public UUID getId() {
        return id;
    }

    public Address() {
    }

    public Address(String street, String number, String zipcode) {
        this.street = street;
        this.number = number;
        this.zipcode = zipcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
