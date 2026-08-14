package com.watch.entity;

import com.watch.exception.DomainException;

public enum CrystalType {
    MINERAL("mineral"),
    SAPPHIRE("sapphire"),
    ACRYLIC("acrylic");

    private final String apiValue;

    CrystalType(String apiValue) {
        this.apiValue = apiValue;
    }

    public String toApi() {
        return apiValue;
    }

    public static CrystalType fromApi(String value) {
        for (CrystalType crystalType : values()) {
            if (crystalType.apiValue.equalsIgnoreCase(value)) {
                return crystalType;
            }
        }

        throw new DomainException("Invalid crystal type: " + value);
    }
}
