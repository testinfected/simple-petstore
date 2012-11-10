package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.billing.CreditCardDetails;
import com.pyxis.petstore.domain.order.LineItem;
import com.pyxis.petstore.domain.order.Order;
import com.pyxis.petstore.domain.order.OrderBook;
import com.pyxis.petstore.domain.order.OrderNumber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.testinfected.petstore.jdbc.Properties.idOf;

// WARNING This is starting to get really ugly and prone to errors
// todo: Eliminate duplication in insert and select statements (see ProductsDatabase, ItemsDatabase)
public class OrdersDatabase implements OrderBook {

    private final Connection connection;

    public OrdersDatabase(Connection connection) {
        this.connection = connection;
    }

    public Order find(OrderNumber orderNumber) {
        Order order = selectOrderAndPayment(orderNumber);
        List<LineItem> lineItems = selectLineItems(order);
        for (LineItem lineItem : lineItems) {
            order.addLineItem(lineItem);
        }
        return order;
    }

    private Order selectOrderAndPayment(OrderNumber orderNumber) {
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select " +
                        "_order.id, _order.number, _order.payment_id, " +
                        "payment.id, payment.payment_type, " +
                        "payment.billing_first_name, payment.billing_last_name, payment.billing_email, " +
                        "payment.card_type, payment.card_number, payment.card_expiry_date " +
                    "from orders _order " +
                    "left outer join payments payment on _order.payment_id = payment.id " +
                    "where _order.number = ?");
            query.setString(1, orderNumber.getNumber());
            ResultSet rs = query.executeQuery();
            rs.next();
            return new OrderRecord().hydrate(rs);
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            close(query);
        }
    }

    private List<LineItem> selectLineItems(Order order) {
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement(
                    "select id, item_number, item_description, item_unit_price, quantity, total_price " +
                    "from line_items " +
                    "where order_id = ? " +
                    "order by order_line");
            query.setLong(1, idOf(order).get());
            ResultSet rs = query.executeQuery();
            List<LineItem> lineItems = new ArrayList<LineItem>();
            while (rs.next()) {
                lineItems.add(new LineItemRecord().hydrate(rs));
            }
            return lineItems;
        } catch (SQLException e) {
            throw new JDBCException("Could not execute query", e);
        } finally {
            close(query);
        }
    }

    public void record(Order order) {
        insertPayment(order);
        insertOrder(order);
        insertLineItems(order);
    }

    private void insertPayment(Order order) {
        if (!order.isPaid()) return;
        insertCreditCard((CreditCardDetails) order.getPaymentMethod());
    }

    private void insertCreditCard(CreditCardDetails creditCard) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(
                    "insert into payments(billing_first_name, billing_last_name, billing_email, " +
                            "card_type, card_number, card_expiry_date, payment_type) values(?, ?, ?, ?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS);
            insert.setString(1, creditCard.getFirstName());
            insert.setString(2, creditCard.getLastName());
            insert.setString(3, creditCard.getEmail());
            insert.setString(4, creditCard.getCardType().name());
            insert.setString(5, creditCard.getCardNumber());
            insert.setString(6, creditCard.getCardExpiryDate());
            insert.setString(7, "credit_card");
            executeInsert(insert);
            ResultSet generatedKeys = insert.getGeneratedKeys();
            generatedKeys.first();
            idOf(creditCard).set(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert credit card details [" + creditCard + "]", e);
        } finally {
            close(insert);
        }
    }

    private void insertOrder(Order order) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement("insert into orders(number, payment_id) values(?, ?)", RETURN_GENERATED_KEYS);
            insert.setString(1, order.getNumber());
            if (order.isPaid()) {
                insert.setLong(2, idOf(order.getPaymentMethod()).get());
            } else {
                insert.setNull(2, Types.BIGINT);
            }
            executeInsert(insert);
            ResultSet generatedKeys = insert.getGeneratedKeys();
            generatedKeys.first();
            idOf(order).set(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert order [" + order + "]", e);
        } finally {
            close(insert);
        }
    }

    private void insertLineItems(Order order) {
        for (LineItem lineItem : order.getLineItems()) {
            insertLineItem(order, lineItem);
        }
    }

    private void insertLineItem(Order order, LineItem lineItem) {
        PreparedStatement insert = null;
        try {
            insert = connection.prepareStatement(
                    "insert into line_items(item_number, item_unit_price, item_description, quantity, total_price, order_id, order_line) " +
                            "values(?, ?, ?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS);
            insert.setString(1, lineItem.getItemNumber());
            insert.setBigDecimal(2, lineItem.getItemUnitPrice());
            insert.setString(3, lineItem.getItemDescription());
            insert.setInt(4, lineItem.getQuantity());
            insert.setBigDecimal(5, lineItem.getTotalPrice());
            insert.setLong(6, idOf(order).get());
            insert.setInt(7, order.getLineItems().indexOf(lineItem));
            executeInsert(insert);
            ResultSet generatedKeys = insert.getGeneratedKeys();
            generatedKeys.first();
            idOf(lineItem).set(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new JDBCException("Could not insert line item [" + lineItem + "]", e);
        } finally {
            close(insert);
        }
    }

    private void executeInsert(PreparedStatement insert) throws SQLException {
        int rowsInserted = insert.executeUpdate();
        if (rowsInserted != 1) {
            throw new SQLException("Unexpected row count of " + rowsInserted + "; expected was 1");
        }
    }

    private void close(Statement statement) {
        if (statement == null) return;
        try {
            statement.close();
        } catch (SQLException ignored) {
        }
    }
}
