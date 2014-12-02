package org.testinfected.petstore.billing;

import org.testinfected.petstore.validation.Validates;
import org.testinfected.petstore.validation.NotNull;

import java.io.Serializable;

public class Address implements Serializable {
    private final NotNull<String> firstName;
    private final NotNull<String> lastName;
    private final String emailAddress;

    public Address(String firstName, String lastName, String emailAddress) {
        this.firstName = Validates.notNull(firstName);
        this.lastName = Validates.notNull(lastName);
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
}
