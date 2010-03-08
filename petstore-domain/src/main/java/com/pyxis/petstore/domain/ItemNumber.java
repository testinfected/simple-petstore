package com.pyxis.petstore.domain;

import javax.persistence.Embeddable;

@Embeddable
public class ItemNumber {

    private String number;

    ItemNumber() {
    }

    public ItemNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override public String toString() {
        return number;
    }
}
