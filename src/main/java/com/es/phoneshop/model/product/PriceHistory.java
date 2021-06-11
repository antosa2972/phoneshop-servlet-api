package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.Calendar;

public class PriceHistory {
    private Calendar date;
    private BigDecimal price;

    public PriceHistory(Calendar date, BigDecimal price) {
        this.date = date;
        this.price = price;
    }

    public String getDate() {
        String dateStr = "";
        dateStr += date.get(Calendar.DAY_OF_MONTH);
        dateStr += " ";
        dateStr += date.get(Calendar.MONTH);
        dateStr += " ";
        dateStr += date.get(Calendar.YEAR);
        return dateStr;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
