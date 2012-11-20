package org.testinfected.petstore.jdbc;

import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.jdbc.records.ItemRecord;
import org.testinfected.petstore.jdbc.records.ProductRecord;
import org.testinfected.petstore.jdbc.support.Insert;
import org.testinfected.petstore.jdbc.support.Select;

import java.sql.Connection;
import java.util.List;

public class ItemsDatabase implements ItemInventory {

    private final Connection connection;
    private final ProductRecord products = new ProductRecord();
    private final ItemRecord items = new ItemRecord(products);

    public ItemsDatabase(Connection connection) {
        this.connection = connection;
    }

    public List<Item> findByProductNumber(String productNumber) {
        return Select.from(items, "item").
                innerJoin(products, "product", "item.product_id = product.id").
                where("product.number = ?", productNumber).
                list(connection);
    }

    public Item find(ItemNumber itemNumber) {
        return Select.from(items, "item").
                innerJoin(products, "product", "item.product_id = product.id").
                where("item.number = ?", itemNumber.getNumber()).
                first(connection);
    }

    public void add(Item item) {
        Insert.into(items, item).execute(connection);
    }
}
