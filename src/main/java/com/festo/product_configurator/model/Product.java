package com.festo.product_configurator.model;

public class Product {

    private Long id;
    private String name;
    private String category;
    private double price;

    public Product() {

    }

    public Product(
            Long id,
            String name,
            String category,
            double price
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }
}
