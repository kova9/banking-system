package com.bankingSystem.bankingSystem.enums;

public enum CustomerId {
    CUSTOMER_ONE("1", "c3b36a50-5e4c-4e0b-9d8e-fd693f278f49"),
    CUSTOMER_TWO("2", "72e637a6-4f3b-4e5d-9531-29b3450de8b8"),
    CUSTOMER_THREE("3", "fb8d4b24-302e-4b59-80a0-97d0c168d437"),
    CUSTOMER_FOUR("4", "4e87dc7c-6c97-4e77-92b1-4f6d58a0be9d"),
    CUSTOMER_FIVE("5", "c805f63b-410d-4916-87cb-23b2df4d5f07"),
    CUSTOMER_SIX("6", "60b6edb8-761d-4d94-b805-905170b319c8"),
    CUSTOMER_SEVEN("7", "42baccb6-5ef7-40c7-8a1a-2f3dce9476e8"),
    CUSTOMER_EIGHT("8", "d8b5c5c9-4c4d-4f3e-8aeb-9268a74d1d4a"),
    CUSTOMER_NINE("9", "de2c9b2d-32a3-420e-8f84-3a51b4c92d61"),
    CUSTOMER_TEN("10", "69e92d14-8c65-4fd7-8b20-4e4ab4b9d0d5");

    private String code;
    private String account;

    CustomerId(String code, String account){
        this.code = code;
        this.account = account;
    }

    public String getCode(){return code;}
    public String getAccount(){return account;}

    public static CustomerId fromCode(String s) {
        for (CustomerId customerId : CustomerId.values()) {
            if (s.trim().equalsIgnoreCase(customerId.code)) {
                return customerId;
            }
        }
        return null;
    }
}
