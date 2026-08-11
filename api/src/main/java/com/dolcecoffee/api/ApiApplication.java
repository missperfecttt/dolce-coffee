package com.dolcecoffee.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    public static class MenuItem {
        private String id;
        private String name;
        private String description;
        private double price;
        private String category;

        public MenuItem(String id, String name, String description, double price, String category) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.category = category;
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }
    }

    @GetMapping("/menu")
    public List<MenuItem> getMenu() {
        return Arrays.asList(
            new MenuItem("1", "Espresso Single", "Rich, dark shot of espresso.", 200.00, "Hot"),
            new MenuItem("2", "Double Espresso", "Bold double shot of espresso.", 200.00, "Hot"),
            new MenuItem("3", "Cappuccino", "Espresso with steamed milk foam.", 200.00, "Hot"),
            new MenuItem("4", "Caffè Latte", "Smooth espresso with warm milk.", 200.00, "Hot"),
            new MenuItem("5", "Iced Vanilla Latte", "Espresso with cold milk and vanilla.", 200.00, "Cold"),
            new MenuItem("6", "Iced Caramel Macchiato", "Espresso, milk, and caramel drizzle.", 200.00, "Cold"),
            new MenuItem("7", "Croissant", "Flaky, buttery fresh croissant.", 200.00, "Pastry"),
            new MenuItem("8", "Chocolate Muffin", "Soft muffin filled with chocolate chunks.", 200.00, "Pastry")
        );
    }
}