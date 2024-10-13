package org.enigma.tokonyadia_api.constant;

public enum OrderStatus {
    DRAFT("Draft"),
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PROCESSING("Processing"),
    COMPLETED("Completed");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public static OrderStatus findByDescription(String description) {
        for (OrderStatus value : values()) {
            if (value.description.equalsIgnoreCase(description)) {
                return value;
            }
        }
        return null;
    }
}
