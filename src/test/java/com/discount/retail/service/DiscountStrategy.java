package com.discount.retail.service;

import java.math.BigDecimal;

public record DiscountStrategy(String name, BigDecimal percentage) {
}
