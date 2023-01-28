package br.com.fastfood.restaurant.models;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    public String name;
}
