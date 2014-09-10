package org.testinfected.petstore.db;

import org.testinfected.petstore.db.support.Insert;
import org.testinfected.petstore.db.support.JDBCException;
import org.testinfected.petstore.db.support.Select;
import org.testinfected.petstore.db.support.Table;
import org.testinfected.petstore.product.DuplicateItemException;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.product.Product;

import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ItemsDatabase implements ItemInventory {

    private final Connection connection;
    private final Table<Product> products = Schema.products();
    private final Table<Item> items = Schema.items(products);

    public ItemsDatabase(Connection connection) {
        this.connection = connection;
    }

    public List<Item> findByProductNumber(String productNumber) {
        return Select.from(items, "item").
                join(products, "product", "item.product_id = product.id").
                where("product.number = ?", productNumber).
                list(connection);
    }

    public Item find(ItemNumber itemNumber) {
        return Select.from(items, "item").
                join(products, "product", "item.product_id = product.id").
                where("item.number = ?", itemNumber.getNumber()).
                first(connection);
    }

    public void add(Item item) throws DuplicateItemException {
        try {
            Insert.into(items, item).execute(connection);
        } catch (JDBCException e) {
            if (e.causedBy(SQLIntegrityConstraintViolationException.class)) throw new DuplicateItemException(item, e.getCause());

            throw e;
        }
    }
}
