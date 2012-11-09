package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.order.LineItem;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.testinfected.petstore.jdbc.Properties.idOf;

// todo: Eliminate duplication in record classes
public class LineItemRecord {

    private static final String LINE_ITEMS_TABLE = "line_items";

    public LineItem hydrate(ResultSet rs) throws SQLException {
        LineItem lineItem = new LineItem(number(rs), description(rs), unitPrice(rs), quantity(rs), totalPrice(rs));
        idOf(lineItem).set(id(rs));
        return lineItem;
    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(getColumnIndex(rs, "id"));
    }

    private String number(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "item_number"));
    }

    private String description(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "item_description"));
    }

    private BigDecimal unitPrice(ResultSet rs) throws SQLException {
        return rs.getBigDecimal(getColumnIndex(rs, "item_unit_price"));
    }

    private int quantity(ResultSet rs) throws SQLException {
        return rs.getInt(getColumnIndex(rs, "quantity"));
    }

    private BigDecimal totalPrice(ResultSet rs) throws SQLException {
        return rs.getBigDecimal(getColumnIndex(rs, "total_price"));
    }

    private int getColumnIndex(ResultSet rs, final String columnName) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equalsIgnoreCase(columnName) &&
                    metaData.getTableName(i).equalsIgnoreCase(LINE_ITEMS_TABLE))
                return i;
        }

        throw new SQLException("Result set has no column '" + columnName + "'");
    }
}
