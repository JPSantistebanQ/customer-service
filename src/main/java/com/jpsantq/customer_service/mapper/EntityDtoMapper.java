package com.jpsantq.customer_service.mapper;

import com.jpsantq.customer_service.domain.Ticker;
import com.jpsantq.customer_service.dto.CustomerInformation;
import com.jpsantq.customer_service.dto.Holding;
import com.jpsantq.customer_service.dto.StockTradeRequest;
import com.jpsantq.customer_service.dto.StockTradeResponse;
import com.jpsantq.customer_service.entity.Customer;
import com.jpsantq.customer_service.entity.PortfolioItem;

import java.util.List;

public class EntityDtoMapper {
    public static CustomerInformation toCustomerInformation(Customer customer, List<PortfolioItem> items) {
        var holdings = items.stream()
                .map(item -> new Holding(item.getTicker(), item.getQuantity()))
                .toList();
        return new CustomerInformation(
                customer.getId(),
                customer.getName(),
                customer.getBalance(),
                holdings);
    }

    public static PortfolioItem toPortfolioItem(Integer customerId, Ticker ticker) {
        var portfolioItem = new PortfolioItem();
        portfolioItem.setCustomerId(customerId);
        portfolioItem.setTicker(ticker);
        portfolioItem.setQuantity(0);
        return portfolioItem;
    }

    public static StockTradeResponse toStockTradeResponse(StockTradeRequest request, Integer customerId, Integer balance) {
        return new StockTradeResponse(
                customerId,
                request.ticker(),
                request.price(),
                request.quantity(),
                request.action(),
                request.totalPrice(),
                balance
        );
    }
}
