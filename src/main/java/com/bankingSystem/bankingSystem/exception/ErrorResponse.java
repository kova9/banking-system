package com.bankingSystem.bankingSystem.exception;

import lombok.Data;

@Data
class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
