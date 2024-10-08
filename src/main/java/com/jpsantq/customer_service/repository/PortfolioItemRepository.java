package com.jpsantq.customer_service.repository;

import com.jpsantq.customer_service.domain.Ticker;
import com.jpsantq.customer_service.entity.PortfolioItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {
    Flux<PortfolioItem> findAllByCustomerId(Integer customerId);

    Mono<PortfolioItem> findByCustomerIdAndTicker(Integer customerId, Ticker ticker);
}
