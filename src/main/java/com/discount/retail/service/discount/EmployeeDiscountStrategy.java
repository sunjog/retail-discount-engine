package com.discount.retail.service.discount;

import com.discount.retail.model.Customer;
import com.discount.retail.model.Role;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EmployeeDiscountStrategy implements PercentageDiscountStrategy {

    public boolean isApplicable(Customer c) {
        return c != null && c.getRole() == Role.EMPLOYEE && !c.isBlacklisted();
    }

    public BigDecimal percentage() {
        return BigDecimal.valueOf(0.30);
    }

    public String name() {
        return "EMPLOYEE";
    }
}