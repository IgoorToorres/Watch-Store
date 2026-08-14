package com.watch.entity;

import com.watch.exception.DomainException;

public enum CaseMaterial {
    STEEL("steel"),
    TITANIUM("titanium"),
    RESIN("resin"),
    BRONZE("bronze"),
    CERAMIC("ceramic");

    private final String apiValue;

    CaseMaterial(String apiValue) {
        this.apiValue = apiValue;
    }

    public String toApi() {
        return apiValue;
    }

    public static CaseMaterial fromApi(String value) {
        for (CaseMaterial caseMaterial : values()) {
            if (caseMaterial.apiValue.equalsIgnoreCase(value)) {
                return caseMaterial;
            }
        }

        throw new DomainException("Invalid case material: " + value);
    }
}
