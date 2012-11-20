package org.testinfected.petstore.jdbc.records;

import org.testinfected.petstore.order.LineItem;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.testinfected.petstore.jdbc.Properties.idOf;
import static org.testinfected.petstore.jdbc.Properties.orderOf;

public class LineItemRecord extends AbstractRecord<LineItem> {

    public static final String TABLE = "line_items";
    public static final String ID = "id";
    public static final String NUMBER = "item_number";
    public static final String DESCRIPTION = "item_description";
    public static final String UNIT_PRICE = "item_unit_price";
    public static final String QUANTITY = "quantity";
    public static final String TOTAL_PRICE = "total_price";
    public static final String ORDER = "order_id";
    public static final String LINE = "order_line";

    @Override
    public String table() {
        return TABLE;
    }

    @Override
    public List<String> columns() {
        return Arrays.asList(ID, NUMBER, DESCRIPTION, UNIT_PRICE, QUANTITY, TOTAL_PRICE, ORDER, LINE);
    }

    @Override
    public LineItem hydrate(ResultSet rs) throws SQLException {
        LineItem lineItem = new LineItem(number(rs), description(rs), unitPrice(rs), quantity(rs), totalPrice(rs));
        idOf(lineItem).set(id(rs));
        return lineItem;
    }

    @Override
    public void dehydrate(PreparedStatement statement, LineItem lineItem) throws SQLException {
        statement.setLong(indexOf(ID), idOf(lineItem).get());
        statement.setString(indexOf(NUMBER), lineItem.getItemNumber());
        statement.setBigDecimal(indexOf(UNIT_PRICE), lineItem.getItemUnitPrice());
        statement.setString(indexOf(DESCRIPTION), lineItem.getItemDescription());
        statement.setInt(indexOf(QUANTITY), lineItem.getQuantity());
        statement.setBigDecimal(indexOf(TOTAL_PRICE), lineItem.getTotalPrice());
        statement.setLong(indexOf(ORDER), idOf(orderOf(lineItem).get()).get());
        statement.setInt(indexOf(LINE), orderOf(lineItem).get().lineOf(lineItem));
    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(findColumn(rs, ID));
    }

    private String number(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, NUMBER));
    }

    private String description(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, DESCRIPTION));
    }

    private BigDecimal unitPrice(ResultSet rs) throws SQLException {
        return rs.getBigDecimal(findColumn(rs, UNIT_PRICE));
    }

    private int quantity(ResultSet rs) throws SQLException {
        return rs.getInt(findColumn(rs, QUANTITY));
    }

    private BigDecimal totalPrice(ResultSet rs) throws SQLException {
        return rs.getBigDecimal(findColumn(rs, TOTAL_PRICE));
    }
}
