package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;

import java.sql.Connection;
import java.util.List;

import static org.testinfected.petstore.jdbc.Sql.matchAnywhere;

public class ProductsDatabase implements ProductCatalog {

    private final Connection connection;
    private final Table<Product> productsTable = Tables.products();

    public ProductsDatabase(Connection connection) {
        this.connection = connection;
    }

    public void add(Product product) {
        Insert.into(productsTable, product).execute(connection);
    }

    public Product findByNumber(String productNumber) {
        Select<Product> select = Select.from(productsTable);
        select.where("number = ?", productNumber);
        return select.single(connection);
    }

    public List<Product> findByKeyword(String keyword) {
        Select<Product> select = Select.from(productsTable);
        select.where("lower(name) like ? or lower(description) like ?", matchAnywhere(keyword), matchAnywhere(keyword));
        return select.list(connection);
    }
}