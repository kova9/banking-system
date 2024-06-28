package com.bankingSystem.bankingSystem.account;

enum AccountType {
    CHECKING("1", "Checking"),
    SAVINGS("2", "Savings");

    private String code;
    private String description;

    AccountType(String code, String description){
        this.code = code;
        this.description = description;
    }

    public String getCode(){
        return code;
    }

    public String getDescription(){
        return description;
    }

    public static AccountType fromCode(String s) {
        for (AccountType accountType : AccountType.values()) {
            if (s.trim().equalsIgnoreCase(accountType.code)) {
                return accountType;
            }
        }
        return null;
    }
}
