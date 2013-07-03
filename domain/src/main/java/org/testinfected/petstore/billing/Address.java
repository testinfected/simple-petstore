package org.testinfected.petstore.billing;

import org.testinfected.petstore.NotNull;
import org.testinfected.petstore.Validate;

import java.io.Serializable;

public class Address implements Serializable {
    private final NotNull<String> firstName;
    private final NotNull<String> lastName;
    private final String emailAddress;

    public Address(String firstName, String lastName, String emailAddress) {
        this.firstName = Validate.notNull(firstName);
        this.lastName = Validate.notNull(lastName);
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!firstName.equals(address.firstName)) return false;
        if (!lastName.equals(address.lastName)) return false;
        if (emailAddress != null ? !emailAddress.equals(address.emailAddress) : address.emailAddress != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        return result;
    }
}
