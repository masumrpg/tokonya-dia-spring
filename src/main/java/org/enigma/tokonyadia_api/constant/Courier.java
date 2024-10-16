package org.enigma.tokonyadia_api.constant;

import lombok.Getter;

@Getter
public enum Courier {
    JNE("Jne"),
    TIKI("Tiki"),
    POS_INDONESIA("Pos Indonesia"),
    WAHANA("Wahana"),
    SI_CEPAT("Si Cepat"),
    J_AND_T("J&T");

    private final String description;

    Courier(String description) {
        this.description = description;
    }

    public static Courier findByDescription(String description) {
        for (Courier value : values()) {
            if (value.description.equalsIgnoreCase(description)) {
                return value;
            }
        }
        return null;
    }
}
