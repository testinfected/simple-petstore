package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.jdbc.records.ProductRecord;
import org.testinfected.petstore.jdbc.support.Insert;
import org.testinfected.petstore.jdbc.support.Record;
import org.testinfected.petstore.jdbc.support.Select;

import java.sql.Connection;
import java.util.List;

import static org.testinfected.petstore.jdbc.support.Sql.matchAnywhere;

public class ProductsDatabase implements ProductCatalog {

    private final Connection connection;
    private final Record<Product> products = new ProductRecord();

    public ProductsDatabase(Connection connection) {
        this.connection = connection;
    }

    public void add(Product product) {
        Insert.into(products, product).execute(connection);
    }

    public Product findByNumber(String productNumber) {
        return Select.from(products).where("number = ?", productNumber).first(connection);
    }

    public List<Product> findByKeyword(String keyword) {
        return Select.from(products).
                where("lower(name) like ? or lower(description) like ?", matchAnywhere(keyword), matchAnywhere(keyword)).
                list(connection);
    }
}