package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.Product;

public final class Tables {

    private Tables() {}

    public static Table<Product> products() {
        Table<Product> table = new Table<Product>("products", new ProductRecord());

        table.addColumn(Column.bigint("id"));
        table.addColumn(Column.varchar("number"));
        table.addColumn(Column.varchar("name"));
        table.addColumn(Column.varchar("description"));
        table.addColumn(Column.varchar("photo_file_name"));

        return table;
    }

    public static Table<Item> items() {
        Table<Item> table = new Table<Item>("items", new ItemRecord());

        table.addColumn(Column.bigint("id"));
        table.addColumn(Column.varchar("number"));
        table.addColumn(Column.bigint("product_id"));
        table.addColumn(Column.decimal("price"));
        table.addColumn(Column.varchar("description"));

        return table;
    }
}
