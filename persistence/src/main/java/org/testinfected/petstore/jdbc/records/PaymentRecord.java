package org.testinfected.petstore.jdbc.records;

import com.pyxis.petstore.domain.billing.Address;
import com.pyxis.petstore.domain.billing.CreditCardDetails;
import com.pyxis.petstore.domain.billing.CreditCardType;
import com.pyxis.petstore.domain.billing.PaymentMethod;
import org.testinfected.petstore.jdbc.support.AbstractRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.testinfected.petstore.jdbc.Properties.idOf;

public class PaymentRecord extends AbstractRecord<PaymentMethod> {

    public static final String TABLE = "payments";

    public static final String ID = "id";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String CARD_TYPE = "card_type";
    public static final String CARD_NUMBER = "card_number";
    public static final String CARD_EXPIRY_DATE = "card_expiry_date";
    public static final String BILLING_FIRST_NAME = "billing_first_name";
    public static final String BILLING_LAST_NAME = "billing_last_name";
    public static final String BILLING_EMAIL = "billing_email";

    public static final String CREDIT_CARD = "credit_card";

    @Override
    public String table() {
        return TABLE;
    }

    @Override
    public List<String> columns() {
        return Arrays.asList(ID, PAYMENT_TYPE, CARD_TYPE, CARD_NUMBER, CARD_EXPIRY_DATE,
                BILLING_FIRST_NAME, BILLING_LAST_NAME, BILLING_EMAIL);
    }

    @Override
    public CreditCardDetails hydrate(ResultSet rs) throws SQLException {
        if (!paymentType(rs).equals(CREDIT_CARD)) throw new IllegalArgumentException("payment of type " + paymentType(rs));

        CreditCardDetails creditCard = new CreditCardDetails(
                CreditCardType.valueOf(cardType(rs)), cardNumber(rs), cardExpiryDate(rs),
                new Address(firstName(rs), lastName(rs), email(rs))
        );
        idOf(creditCard).set(id(rs));
        return creditCard;
    }

    @Override
    public void dehydrate(PreparedStatement statement, PaymentMethod payment) throws SQLException {
        CreditCardDetails creditCard = (CreditCardDetails) payment;
        statement.setLong(indexOf(ID), idOf(creditCard).get());
        statement.setString(indexOf(BILLING_FIRST_NAME), creditCard.getFirstName());
        statement.setString(indexOf(BILLING_LAST_NAME), creditCard.getLastName());
        statement.setString(indexOf(BILLING_EMAIL), creditCard.getEmail());
        statement.setString(indexOf(CARD_TYPE), creditCard.getCardType().name());
        statement.setString(indexOf(CARD_NUMBER), creditCard.getCardNumber());
        statement.setString(indexOf(CARD_EXPIRY_DATE), creditCard.getCardExpiryDate());
        statement.setString(indexOf(PAYMENT_TYPE), CREDIT_CARD);
    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(findColumn(rs, ID));
    }

    private String paymentType(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, PAYMENT_TYPE));
    }

    private String cardType(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, CARD_TYPE));
    }

    private String cardNumber(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, CARD_NUMBER));
    }

    private String cardExpiryDate(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, CARD_EXPIRY_DATE));
    }

    private String firstName(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, BILLING_FIRST_NAME));
    }

    private String lastName(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, BILLING_LAST_NAME));
    }

    private String email(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, BILLING_EMAIL));
    }
}
