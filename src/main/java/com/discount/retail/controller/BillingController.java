package com.discount.retail.controller;

import com.discount.retail.dto.BillRequest;
import com.discount.retail.dto.BillResponse;
import com.discount.retail.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bills")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<BillResponse> calculate(@RequestBody BillRequest req) {
        return ResponseEntity.ok(billingService.calculate(req));
    }
}
