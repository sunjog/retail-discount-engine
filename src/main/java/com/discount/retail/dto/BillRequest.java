package com.discount.retail.dto;

import java.util.List;

public class BillRequest {

    private Long customerId;
    private List<ItemRequest> items;

    public BillRequest() { // default implementation ignored
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long id) {
        this.customerId = id;
    }

    public List<ItemRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemRequest> items) {
        this.items = items;
    }


}
