package org.testinfected.petstore.jdbc;

import org.testinfected.petstore.jdbc.records.ItemRecord;
import org.testinfected.petstore.jdbc.records.ProductRecord;
import org.testinfected.petstore.jdbc.support.Insert;
import org.testinfected.petstore.jdbc.support.Select;
import org.testinfected.petstore.jdbc.support.Table;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.product.Product;

import java.sql.Connection;
import java.util.List;

public class ItemsDatabase implements ItemInventory {

    private final Connection connection;
    private final Table<Product> products = ProductRecord.buildTable();
    private final Table<Item> items = ItemRecord.buildTable(products);

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

    public void add(Item item) {
        Insert.into(items, item).execute(connection);
    }
}
