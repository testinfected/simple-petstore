package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.billing.Address;
import com.pyxis.petstore.domain.billing.CreditCardDetails;
import com.pyxis.petstore.domain.billing.CreditCardType;
import com.pyxis.petstore.domain.billing.PaymentMethod;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.testinfected.petstore.jdbc.Properties.idOf;

public class PaymentRecord {

    private static final String PAYMENTS_TABLE = "payments";
    private static final String CREDIT_CARD = "credit_card";

    public PaymentMethod hydrate(ResultSet rs) throws SQLException {
        if (!paymentType(rs).equals(CREDIT_CARD)) throw new IllegalArgumentException("payment of type " + paymentType(rs));

        CreditCardDetails creditCard = new CreditCardDetails(
                CreditCardType.valueOf(cardType(rs)), cardNumber(rs), cardExpiryDate(rs),
                new Address(firstName(rs), lastName(rs), email(rs))
        );
        idOf(creditCard).set(id(rs));
        return creditCard;
    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(getColumnIndex(rs, "id"));
    }

    private String paymentType(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "payment_type"));
    }

    private String cardType(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "card_type"));
    }

    private String cardNumber(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "card_number"));
    }

    private String cardExpiryDate(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "card_expiry_date"));
    }

    private String firstName(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "billing_first_name"));
    }

    private String lastName(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "billing_last_name"));
    }

    private String email(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "billing_email"));
    }

    private int getColumnIndex(ResultSet rs, final String columnName) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equalsIgnoreCase(columnName) &&
                    metaData.getTableName(i).equalsIgnoreCase(PAYMENTS_TABLE))
                return i;
        }

        throw new SQLException("Result set has no column '" + columnName + "'");
    }
}
