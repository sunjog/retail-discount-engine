package com.discount.retail.service;

import com.discount.retail.model.Customer;
import com.discount.retail.service.discount.PercentageDiscountStrategy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class PercentageDiscountService {
    private final List<PercentageDiscountStrategy> strategies;

    public PercentageDiscountService(List<PercentageDiscountStrategy> strategies) {
        this.strategies = strategies;
    }

    public Optional<PercentageDiscountStrategy> selectBest(Customer customer) {
        return strategies.stream()
                .filter(s -> s.isApplicable(customer))
                .max(Comparator.comparing(PercentageDiscountStrategy::percentage));
    }
}