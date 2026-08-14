package com.watch.entity;

import com.watch.exception.DomainException;

public enum MovementType {
    QUARTZ("quartz"),
    AUTOMATIC("automatic"),
    MANUAL("manual");

    private final String apiValue;

    MovementType(String apiValue) {
        this.apiValue = apiValue;
    }

    public String toApi() {
        return apiValue;
    }

    public static MovementType fromApi(String value) {
        for (MovementType movementType : values()) {
            if (movementType.apiValue.equalsIgnoreCase(value)) {
                return movementType;
            }
        }

        throw new DomainException("Invalid movement type: " + value);
    }
}
