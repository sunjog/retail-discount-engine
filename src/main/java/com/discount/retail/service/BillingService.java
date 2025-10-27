package com.discount.retail.service;

import com.discount.retail.dto.BillRequest;
import com.discount.retail.dto.BillResponse;
import com.discount.retail.dto.ItemRequest;
import com.discount.retail.model.Category;
import com.discount.retail.model.Customer;
import com.discount.retail.model.Item;
import com.discount.retail.repository.CustomerRepository;
import com.discount.retail.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BillingService {

    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final PercentageDiscountService percentageDiscountService;
    private final FlatDiscountService flatDiscountService;

    public BillingService(CustomerRepository customerRepository,
                          ItemRepository itemRepository,
                          PercentageDiscountService percentageDiscountService,
                          FlatDiscountService flatDiscountService) {
        this.customerRepository = customerRepository;
        this.itemRepository = itemRepository;
        this.percentageDiscountService = percentageDiscountService;
        this.flatDiscountService = flatDiscountService;
    }

    public BillResponse calculate(BillRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);

        BigDecimal gross = BigDecimal.ZERO;
        BigDecimal nonGrocery = BigDecimal.ZERO;

        for (ItemRequest ir : request.getItems()) {
            Optional<Item> oi = itemRepository.findById(ir.getItemId());

            if (oi.isEmpty()) continue;

            Item it = oi.get();

            BigDecimal line = it.getPrice().multiply(BigDecimal.valueOf(ir.getQuantity()));

            gross = gross.add(line);

            if (it.getCategory() == Category.NON_GROCERY) nonGrocery = nonGrocery.add(line);
        }

        BillResponse resp = new BillResponse();
        resp.setGrossTotal(gross);
        var opt = percentageDiscountService.selectBest(customer);
        BigDecimal percentDiscount = BigDecimal.ZERO;

        if (opt.isPresent()) {
            var strat = opt.get();
            percentDiscount = nonGrocery.multiply(strat.percentage());
            resp.setAppliedPercentage(strat.name() + " (" + strat.percentage().multiply(BigDecimal.valueOf(100)).intValue() + "%)");
        } else resp.setAppliedPercentage("NONE");
        resp.setPercentageDiscount(percentDiscount);
        BigDecimal afterPercent = gross.subtract(percentDiscount);
        BigDecimal flat = flatDiscountService.computeFlatDiscount(afterPercent);
        resp.setFlatDiscount(flat);
        BigDecimal net = afterPercent.subtract(flat);
        if (net.compareTo(BigDecimal.ZERO) < 0) net = BigDecimal.ZERO;
        resp.setNetPayable(net);

        return resp;
    }

}
