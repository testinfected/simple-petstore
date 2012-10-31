package com.pyxis.petstore.domain.product;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class ItemNumber implements Serializable {

    @NotNull private String number;

    ItemNumber() {}

    public ItemNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemNumber that = (ItemNumber) o;

        if (number != null ? !number.equals(that.number) : that.number != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    public String toString() {
        return "#" + number;
    }
}
