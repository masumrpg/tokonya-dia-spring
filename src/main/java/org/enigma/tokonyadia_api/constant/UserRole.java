package org.enigma.tokonyadia_api.constant;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_ADMIN("Admin"),
    ROLE_SELLER("Pedagang"),
    ROLE_CUSTOMER("Pelanggan");

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
