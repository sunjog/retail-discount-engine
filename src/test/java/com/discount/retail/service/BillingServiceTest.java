package com.discount.retail.service;

import com.discount.retail.dto.BillRequest;
import com.discount.retail.dto.BillResponse;
import com.discount.retail.dto.ItemRequest;
import com.discount.retail.model.*;
import com.discount.retail.repository.CustomerRepository;
import com.discount.retail.repository.ItemRepository;
import com.discount.retail.service.discount.AffiliateDiscountStrategy;
import com.discount.retail.service.discount.EmployeeDiscountStrategy;
import com.discount.retail.service.discount.LoyalCustomerDiscountStrategy;
import com.discount.retail.service.discount.PercentageDiscountStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BillingServiceTest {

    private CustomerRepository customerRepository;
    private ItemRepository itemRepository;
    private PercentageDiscountService percentageDiscountService;
    private FlatDiscountService flatDiscountService;
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        percentageDiscountService = Mockito.mock(PercentageDiscountService.class);
        flatDiscountService = Mockito.mock(FlatDiscountService.class);
        billingService = new BillingService(customerRepository, itemRepository, percentageDiscountService, flatDiscountService);
    }

    private BillRequest createBillRequest(long customerId, long itemId, int qty) {
        BillRequest req = new BillRequest();
        req.setCustomerId(customerId);
        req.setItems(List.of(new ItemRequest(itemId, qty)));
        return req;
    }

    private Customer buildCustomer(long id, Role role, LocalDate joinDate) {
        Customer c = new Customer();
        c.setId(id);
        c.setRole(role);
        c.setLoyaltyStartDate(joinDate);
        c.setBlacklisted(false);
        return c;
    }

    private Item buildItem(long id, String name, Category category, double price) {
        Item i = new Item();
        i.setId(id);
        i.setName(name);
        i.setCategory(category);
        i.setPrice(BigDecimal.valueOf(price));
        return i;
    }

    // 1️⃣ Employee → 30% discount
    @Test
    void testEmployeeGets30PercentDiscount() {
        Customer customer = buildCustomer(1L, Role.EMPLOYEE, LocalDate.now());
        Item item = buildItem(10L, "Jeans", Category.NON_GROCERY, 1000);
        BillRequest req = createBillRequest(1L, 10L, 1);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));

        PercentageDiscountStrategy strat = new EmployeeDiscountStrategy();
        when(percentageDiscountService.selectBest(customer)).thenReturn(Optional.of(strat));
        when(flatDiscountService.computeFlatDiscount(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(35));

        BillResponse resp = billingService.calculate(req);

        assertEquals(BigDecimal.valueOf(1000).setScale(2), resp.getGrossTotal().setScale(2));
        assertEquals(BigDecimal.valueOf(300.0).setScale(1), resp.getPercentageDiscount().setScale(1));
        assertEquals(BigDecimal.valueOf(35).setScale(2), resp.getFlatDiscount().setScale(2));
        assertEquals(BigDecimal.valueOf(665).setScale(2), resp.getNetPayable().setScale(2));
    }

    // 2️⃣ Affiliate → 10% discount
    @Test
    void testAffiliateGets10PercentDiscount() {
        Customer customer = buildCustomer(2L, Role.AFFILIATE, LocalDate.now());
        Item item = buildItem(11L, "Phone", Category.NON_GROCERY, 1000);
        BillRequest req = createBillRequest(2L, 11L, 1);

        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(itemRepository.findById(11L)).thenReturn(Optional.of(item));

        PercentageDiscountStrategy strat = new AffiliateDiscountStrategy();
        when(percentageDiscountService.selectBest(customer)).thenReturn(Optional.of(strat));
        when(flatDiscountService.computeFlatDiscount(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(45));

        BillResponse resp = billingService.calculate(req);
        assertEquals(BigDecimal.valueOf(855).setScale(2), resp.getNetPayable().setScale(2));
    }

    // 3️⃣ Loyal Customer (>2 years) → 5% discount
    @Test
    void testLoyalCustomerGets5PercentDiscount() {
        Customer customer = buildCustomer(3L, Role.CUSTOMER, LocalDate.now().minusYears(3));
        Item item = buildItem(12L, "TV", Category.NON_GROCERY, 1000);
        BillRequest req = createBillRequest(3L, 12L, 1);

        when(customerRepository.findById(3L)).thenReturn(Optional.of(customer));
        when(itemRepository.findById(12L)).thenReturn(Optional.of(item));

        PercentageDiscountStrategy strat = new LoyalCustomerDiscountStrategy();
        when(percentageDiscountService.selectBest(customer)).thenReturn(Optional.of(strat));
        when(flatDiscountService.computeFlatDiscount(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(45));

        BillResponse resp = billingService.calculate(req);
        assertEquals(BigDecimal.valueOf(905).setScale(2), resp.getNetPayable().setScale(2));
    }

    // 4️⃣ Flat Discount → $5 off for every $100
    @Test
    void testFlatDiscountOnlyApplied() {
        Customer customer = buildCustomer(4L, Role.CUSTOMER, LocalDate.now());
        Item item = buildItem(13L, "Speaker", Category.NON_GROCERY, 990);
        BillRequest req = createBillRequest(4L, 13L, 1);

        when(customerRepository.findById(4L)).thenReturn(Optional.of(customer));
        when(itemRepository.findById(13L)).thenReturn(Optional.of(item));

        when(percentageDiscountService.selectBest(customer)).thenReturn(Optional.empty());
        when(flatDiscountService.computeFlatDiscount(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(45));

        BillResponse resp = billingService.calculate(req);
        assertEquals(BigDecimal.valueOf(945).setScale(2), resp.getNetPayable().setScale(2));
    }

    // 5️⃣ Groceries exclusion → no % discount on grocery
    @Test
    void testGroceriesExcludedFromPercentageDiscount() {
        Customer customer = buildCustomer(5L, Role.EMPLOYEE, LocalDate.now());
        Item grocery = buildItem(14L, "Rice", Category.GROCERY, 500);
        Item nongrocery = buildItem(15L, "Jeans", Category.NON_GROCERY, 1000);

        BillRequest req = new BillRequest();
        req.setCustomerId(5L);
        req.setItems(List.of(new ItemRequest(14L, 1), new ItemRequest(15L, 1)));

        when(customerRepository.findById(5L)).thenReturn(Optional.of(customer));
        when(itemRepository.findById(14L)).thenReturn(Optional.of(grocery));
        when(itemRepository.findById(15L)).thenReturn(Optional.of(nongrocery));

        PercentageDiscountStrategy strat = new EmployeeDiscountStrategy();
        when(percentageDiscountService.selectBest(customer)).thenReturn(Optional.of(strat));
        when(flatDiscountService.computeFlatDiscount(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(60));

        BillResponse resp = billingService.calculate(req);
        assertEquals(BigDecimal.valueOf(1140).setScale(2), resp.getNetPayable().setScale(2));
    }

    // 6️⃣ Only highest percentage discount applies
    @Test
    void testOnlyHighestDiscountApplied() {
        Customer customer = buildCustomer(6L, Role.EMPLOYEE, LocalDate.now().minusYears(3));
        Item item = buildItem(16L, "Laptop", Category.NON_GROCERY, 2000);
        BillRequest req = createBillRequest(6L, 16L, 1);

        when(customerRepository.findById(6L)).thenReturn(Optional.of(customer));
        when(itemRepository.findById(16L)).thenReturn(Optional.of(item));

        // EMPLOYEE 30% > LOYAL 5%
        PercentageDiscountStrategy strat = new EmployeeDiscountStrategy();
        when(percentageDiscountService.selectBest(customer)).thenReturn(Optional.of(strat));
        when(flatDiscountService.computeFlatDiscount(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(70));

        BillResponse resp = billingService.calculate(req);
        assertEquals(BigDecimal.valueOf(1330).setScale(2), resp.getNetPayable().setScale(2));
    }
}
