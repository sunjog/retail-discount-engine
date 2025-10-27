package com.discount.retail.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FlatDiscountService {

    public BigDecimal computeFlatDiscount(java.math.BigDecimal total) {
        if (total == null) return BigDecimal.ZERO;

        BigDecimal hundreds = total.divideToIntegralValue(BigDecimal.valueOf(100));

        return hundreds.multiply(BigDecimal.valueOf(5));
    }

}