package com.jpsantq.customer_service.service;

import com.jpsantq.customer_service.dto.CustomerInformation;
import com.jpsantq.customer_service.entity.Customer;
import com.jpsantq.customer_service.exceptions.ApplicationExceptions;
import com.jpsantq.customer_service.mapper.EntityDtoMapper;
import com.jpsantq.customer_service.repository.CustomerRepository;
import com.jpsantq.customer_service.repository.PortfolioItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public CustomerService(CustomerRepository customerRepository, PortfolioItemRepository portfolioItemRepository) {
        this.customerRepository = customerRepository;
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.customerRepository.findById(customerId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
                .flatMap(this::buildCustomerInformation);
    }


    private Mono<CustomerInformation> buildCustomerInformation(Customer customer) {
        return this.portfolioItemRepository.findAllByCustomerId(customer.getId())
                .collectList()
                .map(list -> EntityDtoMapper.toCustomerInformation(customer, list));
    }
}
