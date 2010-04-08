package com.pyxis.petstore.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import static java.lang.String.valueOf;
import static org.apache.commons.lang.StringUtils.leftPad;

@Embeddable
public class OrderNumber {

    @NotNull private String number;

    OrderNumber() {}

    public OrderNumber(String number) {
        this.number = number;
    }

    public OrderNumber(long number) {
        this(leftPad(valueOf(number), 8, "0"));
    }

    public String getNumber() {
        return number;
    }

    @Override public String toString() {
        return number;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderNumber that = (OrderNumber) o;

        if (number != null ? !number.equals(that.number) : that.number != null) return false;

        return true;
    }

    @Override public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }
}
