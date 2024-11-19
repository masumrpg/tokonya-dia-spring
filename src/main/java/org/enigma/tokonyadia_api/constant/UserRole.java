package org.enigma.tokonyadia_api.constant;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_ADMIN("Admin"),
    ROLE_SELLER("Seller"),
    ROLE_CUSTOMER("Customer");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public static UserRole findByDescription(String description) {
        for (UserRole value : values()) {
            if (value.description.equalsIgnoreCase(description)) {
                return value;
            }
        }
        return null;
    }
}
