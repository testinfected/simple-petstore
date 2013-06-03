package org.testinfected.petstore.db.records;

import org.testinfected.petstore.billing.Address;
import org.testinfected.petstore.billing.CreditCardDetails;
import org.testinfected.petstore.billing.CreditCardType;
import org.testinfected.petstore.billing.PaymentMethod;
import org.testinfected.petstore.db.support.Column;
import org.testinfected.petstore.db.support.Table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testinfected.petstore.db.Access.idOf;

public class PaymentRecord extends AbstractRecord<PaymentMethod> {

    private final Table<PaymentMethod> payments = new Table<PaymentMethod>("payments", this);

    private final Column<Long> id = payments.LONG("id");
    private final Column<String> paymentType = payments.STRING("payment_type");
    private final Column<String> cardType = payments.STRING("card_type");
    private final Column<String> cardNumber = payments.STRING("card_number");
    private final Column<String> cardExpiryDate = payments.STRING("card_expiry_date");
    private final Column<String> firstName = payments.STRING("billing_first_name");
    private final Column<String> lastName = payments.STRING("billing_last_name");
    private final Column<String> email = payments.STRING("billing_email");

    public static final String CREDIT_CARD = "credit_card";

    public static Table<PaymentMethod> buildTable() {
        return new PaymentRecord().payments;
    }

    @Override
    public CreditCardDetails hydrate(ResultSet rs) throws SQLException {
        if (!paymentType.get(rs).equals(CREDIT_CARD)) throw new IllegalArgumentException("payment of type " + paymentType.get(rs));

        CreditCardDetails creditCard = new CreditCardDetails(
                CreditCardType.valueOf(cardType.get(rs)), cardNumber.get(rs), cardExpiryDate.get(rs),
                new Address(firstName.get(rs), lastName.get(rs), email.get(rs))
        );
        idOf(creditCard).set(id.get(rs));
        return creditCard;
    }

    @Override
    public void dehydrate(PreparedStatement st, PaymentMethod payment) throws SQLException {
        CreditCardDetails creditCard = (CreditCardDetails) payment;
        id.set(st, idOf(creditCard).get());
        firstName.set(st, creditCard.getFirstName());
        lastName.set(st, creditCard.getLastName());
        email.set(st, creditCard.getEmail());
        cardType.set(st, creditCard.getCardType().name());
        cardNumber.set(st, creditCard.getCardNumber());
        cardExpiryDate.set(st, creditCard.getCardExpiryDate());
        paymentType.set(st, CREDIT_CARD);
    }
}
