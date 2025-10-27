package com.discount.retail.service.discount;

import com.discount.retail.model.Customer;
import com.discount.retail.model.Role;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AffiliateDiscountStrategy implements PercentageDiscountStrategy {

    public boolean isApplicable(Customer c) {
        return c != null && c.getRole() == Role.AFFILIATE && !c.isBlacklisted();
    }

    public BigDecimal percentage() {
        return BigDecimal.valueOf(0.10);
    }

    public String name() {
        return "AFFILIATE";
    }
}