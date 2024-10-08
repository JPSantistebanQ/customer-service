package com.jpsantq.customer_service.repository;

import com.jpsantq.customer_service.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
