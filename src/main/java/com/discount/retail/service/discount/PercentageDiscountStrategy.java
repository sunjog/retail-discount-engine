package com.discount.retail.service.discount;

import com.discount.retail.model.Customer;

import java.math.BigDecimal;

public interface PercentageDiscountStrategy {

    boolean isApplicable(Customer customer);

    BigDecimal percentage();

    String name();
}