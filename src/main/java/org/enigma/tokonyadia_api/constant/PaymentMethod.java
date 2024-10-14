package org.enigma.tokonyadia_api.constant;

public enum PaymentMethod {
    BANK_TRANSFER("Bank Transfer"),
    CREDIT_CARD("Credit Card"),
    E_WALLET("E-Wallet"),
    GOPAY("Gopay"),
    OVO("Ovo");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public static PaymentMethod findByDescription(String description) {
        for (PaymentMethod value : values()) {
            if (value.description.equalsIgnoreCase(description)) {
                return value;
            }
        }
        return null;
    }
}