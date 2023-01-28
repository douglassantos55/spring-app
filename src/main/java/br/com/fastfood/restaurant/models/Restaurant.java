package br.com.fastfood.restaurant.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Entity
@Validated
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Size(min = 3, max = 300)
    public String name;
}
