package org.testinfected.petstore.product;

import java.io.Serializable;

public class ItemNumber implements Serializable {

    private final String number;

    public ItemNumber(String number) {
        this.number = number;
    }

    public String number() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemNumber that = (ItemNumber) o;

        return !(number != null ? !number.equals(that.number) : that.number != null);

    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    public String toString() {
        return "#" + number;
    }
}
