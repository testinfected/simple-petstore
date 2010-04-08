package test.support.com.pyxis.petstore.builders;

import com.pyxis.petstore.domain.billing.Account;

public class AccountBuilder implements Builder<Account> {

    private String firstName;
    private String lastName;
    private String emailAddress;

    public static AccountBuilder anAccount() {
        return new AccountBuilder();
    }

    public Account build() {
        return new Account(firstName, lastName, emailAddress);
    }

    public AccountBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AccountBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AccountBuilder withEmail(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }
}