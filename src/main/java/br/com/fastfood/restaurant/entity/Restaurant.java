package br.com.fastfood.restaurant.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    public static final Path UPLOAD_PATH = Path.of("upload", "restaurant", "logo");

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty
    @Size(min = 3, message = "must have at least {min} characters")
    private String name;

    @NotEmpty
    private String description;

    private String logo;

    @NotEmpty(message = "must have at least one item")
    @Valid
    @OneToMany(targetEntity = Menu.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menu = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public UUID getId() {
        return id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setMenu(List<Menu> menu) {
        this.menu.clear();
        for (Menu item : menu) {
            item.setRestaurant(this);
        }
        this.menu.addAll(menu);
    }
}