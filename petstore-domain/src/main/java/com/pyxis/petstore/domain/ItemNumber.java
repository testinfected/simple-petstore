package com.pyxis.petstore.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;


public @Embeddable class ItemNumber {

    private @NotNull String number;

    ItemNumber() {}

    public ItemNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public String toString() {
        return number;
    }
}
