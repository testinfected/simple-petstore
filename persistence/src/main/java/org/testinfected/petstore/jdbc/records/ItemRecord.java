package org.testinfected.petstore.jdbc.records;

import org.testinfected.petstore.jdbc.support.Column;
import org.testinfected.petstore.jdbc.support.Table;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.product.Product;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testinfected.petstore.jdbc.Properties.idOf;
import static org.testinfected.petstore.jdbc.Properties.productOf;

public class ItemRecord extends AbstractRecord<Item> {

    private final Table<Product> products;

    private final Table<Item> items = new Table<Item>("items", this);

    private final Column<Long> id = items.LONG("id");
    private final Column<String> number = items.STRING("number");
    private final Column<Long> product = items.LONG("product_id");
    private final Column<BigDecimal> price = items.BIG_DECIMAL("price");
    private final Column<String> description = items.STRING("description");

    public static Table<Item> buildTable(Table<Product> products) {
        return new ItemRecord(products).items;
    }

    public ItemRecord(Table<Product> products) {
        this.products = products;
    }

    @Override
    public Item hydrate(ResultSet rs) throws SQLException {
        Item item = new Item(new ItemNumber(number.get(rs)), products.hydrate(rs), price.get(rs));
        item.setDescription(description.get(rs));
        idOf(item).set(id.get(rs));
        return item;
    }

    @Override
    public void dehydrate(PreparedStatement st, Item item) throws SQLException {
        id.set(st, idOf(item).get());
        number.set(st, item.getNumber());
        product.set(st, idOf(productOf(item).get()).get());
        price.set(st, item.getPrice());
        description.set(st, item.getDescription());
    }
}
