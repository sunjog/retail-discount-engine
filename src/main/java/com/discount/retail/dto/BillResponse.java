package com.discount.retail.dto;

import java.math.BigDecimal;

public class BillResponse {

    private BigDecimal grossTotal;
    private BigDecimal percentageDiscount;
    private BigDecimal flatDiscount;
    private BigDecimal netPayable;
    private String appliedPercentage;

    public BillResponse() { // default implementation ignored
    }

    public BigDecimal getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(BigDecimal g) {
        this.grossTotal = g;
    }

    public BigDecimal getPercentageDiscount() {
        return percentageDiscount;
    }

    public void setPercentageDiscount(BigDecimal p) {
        this.percentageDiscount = p;
    }

    public BigDecimal getFlatDiscount() {
        return flatDiscount;
    }

    public void setFlatDiscount(BigDecimal f) {
        this.flatDiscount = f;
    }

    public BigDecimal getNetPayable() {
        return netPayable;
    }

    public void setNetPayable(BigDecimal n) {
        this.netPayable = n;
    }

    public String getAppliedPercentage() {
        return appliedPercentage;
    }

    public void setAppliedPercentage(String s) {
        this.appliedPercentage = s;
    }
}
