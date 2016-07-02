package org.testinfected.petstore.db;

import com.vtence.tape.Insert;
import com.vtence.tape.JDBCException;
import com.vtence.tape.Select;
import com.vtence.tape.Table;
import org.testinfected.petstore.product.DuplicateProductException;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;

import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public class ProductsDatabase implements ProductCatalog {

    private final Connection connection;
    private final Table<Product> products = Schema.products();

    public ProductsDatabase(Connection connection) {
        this.connection = connection;
    }

    public void add(Product product) throws DuplicateProductException {
        try {
            Insert.into(products, product).execute(connection);
        } catch (JDBCException e) {
            if (e.causedBy(SQLIntegrityConstraintViolationException.class))
                throw new DuplicateProductException(product, e.getCause());
            throw e;
        }
    }

    public Product findByNumber(String productNumber) {
        return Select.from(products).where("number = ?", productNumber).first(connection);
    }

    public List<Product> findByKeyword(String keyword) {
        return Select.from(products).
                where("lower(name) like ? or lower(description) like ?", matchAnywhere(keyword), matchAnywhere(keyword)).
                list(connection);
    }

    private static String matchAnywhere(final String pattern) {
        return "%" + pattern + "%";
    }
}