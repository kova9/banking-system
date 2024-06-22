package com.bankingSystem.bankingSystem.exception;

import org.springframework.http.HttpStatus;

public class BankingSystemException extends RuntimeException {
    private HttpStatus status;

    private BankingSystemException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static BankingSystemExceptionBuilder badRequest() {
        return new BankingSystemExceptionBuilder(HttpStatus.BAD_REQUEST);
    }

    public static BankingSystemExceptionBuilder notFound() {
        return new BankingSystemExceptionBuilder(HttpStatus.NOT_FOUND);
    }

    public static class BankingSystemExceptionBuilder {
        private String message;
        private HttpStatus status;

        public BankingSystemExceptionBuilder(HttpStatus status) {
            this.status = status;
        }

        public BankingSystemExceptionBuilder message(String message) {
            this.message = message;
            return this;
        }

        public BankingSystemException build() {
            return new BankingSystemException(message, status);
        }
    }
}

