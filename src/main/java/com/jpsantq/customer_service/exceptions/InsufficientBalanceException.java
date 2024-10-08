package com.jpsantq.customer_service.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    private static final String MESSAGE = "Customer [id: %d] is not have enough founds to complete the transaction";

    public InsufficientBalanceException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
