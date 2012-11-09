package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemNumber;
import com.pyxis.petstore.domain.product.Product;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.testinfected.petstore.jdbc.Properties.idOf;

// todo: Eliminate duplication in record classes
public class ItemRecord implements Record<Item> {

    private static final String ITEMS_TABLE = "items";

    public Item hydrate(ResultSet rs) throws SQLException {
        Product product = new ProductRecord().hydrate(rs);
        Item item = new Item(new ItemNumber(number(rs)), product, price(rs));
        item.setDescription(description(rs));
        idOf(item).set(id(rs));
        return item;
    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(getColumnIndex(rs, "id"));
    }

    private String number(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "number"));
    }

    private BigDecimal price(ResultSet rs) throws SQLException {
        return rs.getBigDecimal(getColumnIndex(rs, "price"));
    }

    private String description(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "description"));
    }

    private int getColumnIndex(ResultSet rs, final String columnName) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equalsIgnoreCase(columnName) &&
                    metaData.getTableName(i).equalsIgnoreCase(ITEMS_TABLE))
                return i;
        }

        throw new SQLException("Result set has no column '" + columnName + "'");
    }
}
