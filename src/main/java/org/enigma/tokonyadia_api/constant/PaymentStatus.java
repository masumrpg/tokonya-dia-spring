package org.enigma.tokonyadia_api.constant;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("Pending"),
    ON_PROCESS("On Process"),
    CANCELED("Canceled"),
    FAILED("Canceled"),
    COMPLETED("Completed");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public static PaymentStatus findByDescription(String description) {
        for (PaymentStatus value : values()) {
            if (value.description.equalsIgnoreCase(description)) {
                return value;
            }
        }
        return null;
    }
}
