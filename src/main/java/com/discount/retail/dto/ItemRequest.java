package com.discount.retail.dto;

public class ItemRequest {

    private Long itemId;
    private int quantity;

    public ItemRequest() { // default implementation ignored
    }

    public ItemRequest(Long itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long id) {
        this.itemId = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }
}