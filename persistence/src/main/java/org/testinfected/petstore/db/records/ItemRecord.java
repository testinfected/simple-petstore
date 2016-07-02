package org.testinfected.petstore.db.records;

import com.vtence.tape.Column;
import com.vtence.tape.Record;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.product.Product;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testinfected.petstore.db.Access.idOf;
import static org.testinfected.petstore.db.Access.productOf;

public class ItemRecord extends AbstractRecord<Item> {

    private final Column<Long> id;
    private final Column<String> number;
    private final Column<Long> product;
    private final Column<BigDecimal> price;
    private final Column<String> description;

    private final Record<Product> products;

    public ItemRecord(Column<Long> id,
                      Column<String> number,
                      Column<Long> product,
                      Column<BigDecimal> price,
                      Column<String> description,
                      Record<Product> products) {
        this.id = id;
        this.number = number;
        this.product = product;
        this.price = price;
        this.description = description;
        this.products = products;
    }

    public Item hydrate(ResultSet rs) throws SQLException {
        Item item = new Item(new ItemNumber(number.get(rs)), products.hydrate(rs), price.get(rs));
        item.setDescription(description.get(rs));
        idOf(item).set(id.get(rs));
        return item;
    }

    public void dehydrate(PreparedStatement st, Item item) throws SQLException {
        id.set(st, idOf(item).get());
        number.set(st, item.getNumber());
        product.set(st, idOf(productOf(item).get()).get());
        price.set(st, item.getPrice());
        description.set(st, item.getDescription());
    }
}
