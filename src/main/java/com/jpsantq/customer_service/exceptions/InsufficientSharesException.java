package com.jpsantq.customer_service.exceptions;

public class InsufficientSharesException extends RuntimeException {
    private static final String MESSAGE = "Customer [id: %d] is not have enough shares to complete the transaction";

    public InsufficientSharesException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
