package com.bankingSystem.bankingSystem.account;

public enum AccountId {
    ACCOUNT_ONE("1", "d6a8f71c-6c29-4f62-9653-8a4b919e8c88"),
    ACCOUNT_TWO("2", "4bfec43e-89ed-4094-b8e0-4a217c7a8c33"),
    ACCOUNT_THREE("3", "0c1f4c2e-c08c-4e4a-9a27-2c1a66d70c92"),
    ACCOUNT_FOUR("4", "d24d0648-1c8e-4f02-9e2d-458d2849f92d"),
    ACCOUNT_FIVE("5", "bc24903b-4208-43e0-92f1-1b5a3f4c12f5"),
    ACCOUNT_SIX("6", "87f2b194-9582-4c56-b848-1d8b4e66c0a3"),
    ACCOUNT_SEVEN("7", "ad0c60b3-fd6a-4ab7-a6b7-995f9cf7dbeb"),
    ACCOUNT_EIGHT("8", "43890d5b-d1ff-40ad-a7b2-0c4a024d3093"),
    ACCOUNT_NINE("9", "fa25728c-3f21-4f5d-8c8e-9c1a3782f8e7"),
    ACCOUNT_TEN("10", "b9a8b514-60d1-45f1-9a02-5f2e0a7654b5");

    private String code;
    private String account;

    AccountId(String code, String account){
        this.code = code;
        this.account = account;
    }

    public String getCode(){return code;}
    public String getAccount(){return account;}

    public static AccountId fromCode(String s) {
        for (AccountId accountId : AccountId.values()) {
            if (s.trim().equalsIgnoreCase(accountId.code)) {
                return accountId;
            }
        }
        return null;
    }

}
