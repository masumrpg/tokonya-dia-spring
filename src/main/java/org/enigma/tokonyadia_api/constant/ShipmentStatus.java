package org.enigma.tokonyadia_api.constant;

import lombok.Getter;

@Getter
public enum ShipmentStatus {
    PENDING("Pending"),
    ON_PROCESS("On Process"),
    CANCELLED("Cancelled"),
    FAILED("Failed"),
    DELIVERED("Delivered");

    private final String description;

    ShipmentStatus(String description) {
        this.description = description;
    }

    public static ShipmentStatus findByDescription(String description) {
        for (ShipmentStatus value : values()) {
            if (value.description.equalsIgnoreCase(description)) {
                return value;
            }
        }
        return null;
    }
}
