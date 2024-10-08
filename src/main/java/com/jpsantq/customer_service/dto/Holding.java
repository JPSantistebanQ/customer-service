package com.jpsantq.customer_service.dto;

import com.jpsantq.customer_service.domain.Ticker;

public record Holding(Ticker ticker,
                      Integer quantity) {
}
