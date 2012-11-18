package org.testinfected.petstore.jdbc.records;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemNumber;
import com.pyxis.petstore.domain.product.Product;
import org.testinfected.petstore.jdbc.support.AbstractRecord;
import org.testinfected.petstore.jdbc.support.Record;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.testinfected.petstore.jdbc.Properties.idOf;
import static org.testinfected.petstore.jdbc.Properties.productOf;

public class ItemRecord extends AbstractRecord<Item> {

    public static final String TABLE = "items";

    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String PRODUCT = "product_id";
    public static final String PRICE = "price";
    public static final String DESCRIPTION = "description";

    private final Record<Product> products;

    public ItemRecord(Record<Product> products) {
        this.products = products;
    }

    @Override
    public String table() {
        return TABLE;
    }

    @Override
    public List<String> columns() {
        return Arrays.asList(ID, NUMBER, PRODUCT, PRICE, DESCRIPTION);
    }

    @Override
    public Item hydrate(ResultSet rs) throws SQLException {
        Item item = new Item(new ItemNumber(number(rs)), product(rs), price(rs));
        item.setDescription(description(rs));
        idOf(item).set(id(rs));
        return item;
    }

    @Override
    public void dehydrate(PreparedStatement statement, Item item) throws SQLException {
        statement.setLong(indexOf(ID), idOf(item).get());
        statement.setString(indexOf(NUMBER), item.getNumber());
        statement.setLong(indexOf(PRODUCT), idOf(productOf(item).get()).get());
        statement.setBigDecimal(indexOf(PRICE), item.getPrice());
        statement.setString(indexOf(DESCRIPTION), item.getDescription());
    }

    private String number(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, NUMBER));
    }

    private Product product(ResultSet rs) throws SQLException {
        return products.hydrate(rs);
    }

    private BigDecimal price(ResultSet rs) throws SQLException {
        return rs.getBigDecimal(findColumn(rs, PRICE));
    }

    private String description(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, DESCRIPTION));
    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(findColumn(rs, ID));
    }
}
