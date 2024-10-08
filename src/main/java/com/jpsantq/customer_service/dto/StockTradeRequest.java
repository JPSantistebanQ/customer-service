package com.jpsantq.customer_service.dto;

import com.jpsantq.customer_service.domain.Ticker;
import com.jpsantq.customer_service.domain.TradeAction;

public record StockTradeRequest(Ticker ticker,
                                Integer price,
                                Integer quantity,
                                TradeAction action) {

    public Integer totalPrice() {
        return price * quantity;
    }
}
