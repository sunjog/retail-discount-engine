package com.discount.retail.service.discount;

import com.discount.retail.model.Customer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class LoyalCustomerDiscountStrategy implements PercentageDiscountStrategy {

    public boolean isApplicable(Customer c) {
        if (c == null || c.isBlacklisted() || c.getLoyaltyStartDate() == null) return false;
        return c.getLoyaltyStartDate().isBefore(LocalDate.now().minusYears(2));
    }

    public BigDecimal percentage() {
        return BigDecimal.valueOf(0.05);
    }

    public String name() {
        return "LOYAL_CUSTOMER";
    }
}