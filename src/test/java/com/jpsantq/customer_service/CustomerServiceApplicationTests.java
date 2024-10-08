package com.jpsantq.customer_service;

import com.jpsantq.customer_service.domain.Ticker;
import com.jpsantq.customer_service.domain.TradeAction;
import com.jpsantq.customer_service.dto.StockTradeRequest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@Log4j2
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerServiceApplicationTests {

    @Autowired
    private WebTestClient client;

    @Test
    void customerInformation() {
        getCustomer(1, HttpStatus.OK)
                .jsonPath("$.name").isEqualTo("Sam")
                .jsonPath("$.balance").isEqualTo(10_000)
                .jsonPath("$.holdings").isEmpty();
    }

    @Test
    void buyAndSell() {
        // * buy
        var buyRequest1 = new StockTradeRequest(Ticker.GOOGLE, 100, 5, TradeAction.BUY);
        trade(2, buyRequest1, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(9_500)
                .jsonPath("$.totalPrice").isEqualTo(500);

        var buyRequest2 = new StockTradeRequest(Ticker.GOOGLE, 100, 10, TradeAction.BUY);
        trade(2, buyRequest2, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(8_500)
                .jsonPath("$.totalPrice").isEqualTo(1_000);

        // * Check holdings
        getCustomer(2, HttpStatus.OK)
                .jsonPath("$.holdings").isNotEmpty()
                .jsonPath("$.holdings.length()").isEqualTo(1)
                .jsonPath("$.holdings.[0].ticker").isEqualTo("GOOGLE")
                .jsonPath("$.holdings.[0].quantity").isEqualTo(15);

        // * Sell
        var sellRequest1 = new StockTradeRequest(Ticker.GOOGLE, 110, 5, TradeAction.SELL);
        trade(2, sellRequest1, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(9_050)
                .jsonPath("$.totalPrice").isEqualTo(550);

        var sellRequest2 = new StockTradeRequest(Ticker.GOOGLE, 110, 10, TradeAction.SELL);
        trade(2, sellRequest2, HttpStatus.OK)
                .jsonPath("$.balance").isEqualTo(10_150)
                .jsonPath("$.totalPrice").isEqualTo(1_100);

        // * Check holdings
        getCustomer(2, HttpStatus.OK)
                .jsonPath("$.holdings").isNotEmpty()
                .jsonPath("$.holdings.length()").isEqualTo(1)
                .jsonPath("$.holdings.[0].ticker").isEqualTo("GOOGLE")
                .jsonPath("$.holdings.[0].quantity").isEqualTo(0);
    }

    @Test
    void customerNotFound() {
        getCustomer(10, HttpStatus.NOT_FOUND)
                .jsonPath("$.detail").isEqualTo("Customer [id: 10] not found");

        // * Sell
        var sellRequest1 = new StockTradeRequest(Ticker.GOOGLE, 110, 5, TradeAction.SELL);
        trade(10, sellRequest1, HttpStatus.NOT_FOUND)
                .jsonPath("$.detail").isEqualTo("Customer [id: 10] not found");
    }

    private WebTestClient.BodyContentSpec getCustomer(Integer customerId, HttpStatus expectedStatus) {
        return this.client.get()
                .uri("/customers/{customerId}", customerId)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(e -> log.info("Response: {}", new String(Objects.requireNonNull(e.getResponseBody()))));
    }

    @Test
    void insufficientBalance() {
        // * buy
        var buyRequest1 = new StockTradeRequest(Ticker.GOOGLE, 100, 101, TradeAction.BUY);
        trade(3, buyRequest1, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Customer [id: 3] is not have enough founds to complete the transaction");
    }

    @Test
    void insufficientShares() {
        // * buy
        var buyRequest1 = new StockTradeRequest(Ticker.GOOGLE, 100, 1, TradeAction.SELL);
        trade(3, buyRequest1, HttpStatus.BAD_REQUEST)
                .jsonPath("$.detail").isEqualTo("Customer [id: 3] is not have enough shares to complete the transaction");
    }

    private WebTestClient.BodyContentSpec trade(Integer customerId, StockTradeRequest request, HttpStatus expectedStatus) {
        return this.client.post()
                .uri("/customers/{customerId}/trade", customerId)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody()
                .consumeWith(e -> log.info("Response: {}", new String(Objects.requireNonNull(e.getResponseBody()))));
    }
}
