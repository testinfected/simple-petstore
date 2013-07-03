package test.unit.org.testinfected.petstore.billing;

import org.junit.Test;
import org.testinfected.petstore.billing.Address;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.aValidAddress;
import static test.support.org.testinfected.petstore.builders.AddressBuilder.anAddress;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.on;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.succeeds;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.validationOf;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.violates;
import static test.support.org.testinfected.petstore.matchers.ValidationMatchers.withMessage;

public class AddressTest {

    String MISSING = null;

    @Test public void
    addressesMatchWhenAllPropertiesMatch() {
        Address address = anAddress().
                withFirstName("John").
                withLastName("Doe").
                withEmail("jdoe@gmail.com").build();
        Address shouldMatch = anAddress().
                withFirstName("John").
                withLastName("Doe").
                withEmail("jdoe@gmail.com").build();
        Address shouldNotMatch = anAddress().
                withFirstName("Jane").
                withLastName("Doe").
                withEmail("jdoe@gmail.com").build();
        assertThat("address", address, equalTo(shouldMatch));
        assertThat("hash code", address.hashCode(), equalTo(shouldMatch.hashCode()));
        assertThat("address ", address, not(equalTo(shouldNotMatch)));
    }

    @Test public void
    isInvalidWithoutAFirstName() {
        assertThat("validation of address with missing first name", validationOf(anAddress().withFirstName(MISSING)), violates(on("firstName"), withMessage("missing")));
    }

    @Test public void
    isInvalidWithoutALastName() {
        assertThat("validation of address with missing last name", validationOf(anAddress().withLastName(MISSING)), violates(on("lastName"), withMessage("missing")));
    }

    @Test public void
    isValidWithAFullName() {
        assertThat("validation of valid address", validationOf(aValidAddress()), succeeds());
    }
}
