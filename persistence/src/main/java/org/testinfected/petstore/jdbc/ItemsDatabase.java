package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ItemNumber;

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
        Select<Item> select = Select.from(items, "item");
        select.innerJoin(products, "product", "item.product_id = product.id");
        select.where("product.number = ?", productNumber);
        return select.list(connection);
    }

    public Item find(ItemNumber itemNumber) {
        Select<Item> select = Select.from(items, "item");
        select.innerJoin(products, "product", "item.product_id = product.id");
        select.where("item.number = ?", itemNumber.getNumber());
        return select.single(connection);
    }

    public void add(Item item) {
        Insert.into(items, item).execute(connection);
    }
}
