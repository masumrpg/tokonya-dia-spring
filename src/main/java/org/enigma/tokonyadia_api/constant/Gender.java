package org.enigma.tokonyadia_api.constant;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public static Gender findByDescription(String description) {
        for (Gender value : values()) {
            if (value.description.equalsIgnoreCase(description)) {
                return value;
            }
        }
        return null;
    }
}
