package com.discount.retail.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ITEM")
public class Item {

    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;
    private BigDecimal price;

    public Item() {
    }

    public Item(Long id, String name, Category category, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
