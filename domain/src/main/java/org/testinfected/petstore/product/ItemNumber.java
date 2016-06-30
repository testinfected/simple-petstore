package org.testinfected.petstore.product;

import java.io.Serializable;

public class ItemNumber implements Serializable {

    private final String number;

    public ItemNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemNumber that = (ItemNumber) o;

        return number != null ? number.equals(that.number) : that.number == null;
    }

    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    public String toString() {
        return "#" + number;
    }
}
