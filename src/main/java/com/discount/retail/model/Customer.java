package com.discount.retail.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    private LocalDate loyaltyStartDate;
    private boolean blacklisted;

    public Customer() {
    }

    public Customer(Long id, String name, Role role, LocalDate loyaltyStartDate, boolean blacklisted) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.loyaltyStartDate = loyaltyStartDate;
        this.blacklisted = blacklisted;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getLoyaltyStartDate() {
        return loyaltyStartDate;
    }

    public void setLoyaltyStartDate(LocalDate loyaltyStartDate) {
        this.loyaltyStartDate = loyaltyStartDate;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        this.blacklisted = blacklisted;
    }
}
