package test.support.com.pyxis.petstore.web;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.jdbc.ConnectionSource;
import org.testinfected.petstore.jdbc.DriverManagerConnectionSource;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import org.testinfected.petstore.jdbc.UnitOfWork;
import test.support.org.testinfected.petstore.jdbc.DatabaseCleaner;
import test.support.org.testinfected.petstore.jdbc.DatabaseConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseDriver {

    private final Connection connection;
    private final ProductCatalog productCatalog;

    public static DatabaseDriver configure(DatabaseConfiguration configuration) {
        ConnectionSource connectionSource = new DriverManagerConnectionSource(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());
        return new DatabaseDriver(connectionSource.connect());
    }

    public DatabaseDriver(Connection connection) {
        this.connection = connection;
        this.productCatalog = new ProductsDatabase(connection);
    }

    public void clean() throws Exception {
        new DatabaseCleaner(connection).clean();
    }

    public void stop() throws SQLException {
        connection.close();
    }

    public void addProducts(Iterable<Product> products) throws Exception {
        for (Product product : products) {
            addProduct(product);
        }
    }

    public void addProduct(final Product product) throws Exception {
        new JDBCTransactor(connection).perform(new UnitOfWork() {
            public void execute() throws Exception {
                productCatalog.add(product);
            }
        });
    }
}
