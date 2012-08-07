package test.support.com.pyxis.petstore.web;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.jdbc.DataSourceProperties;
import org.testinfected.petstore.jdbc.DriverManagerDataSource;
import org.testinfected.petstore.jdbc.JDBCTransactor;
import org.testinfected.petstore.jdbc.ProductsDatabase;
import org.testinfected.petstore.jdbc.UnitOfWork;
import test.support.org.testinfected.petstore.jdbc.DatabaseCleaner;
import test.support.org.testinfected.petstore.jdbc.DatabaseMigrator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseDriver {

    private final DataSource dataSource;
    private final DatabaseMigrator migrator;

    private Connection connection;
    private ProductCatalog productCatalog;

    private DatabaseCleaner cleaner;

    public static DatabaseDriver create(DataSourceProperties properties) {
        return new DatabaseDriver(DriverManagerDataSource.from(properties));
    }

    public DatabaseDriver(DataSource dataSource) {
        this.dataSource = dataSource;
        this.migrator = new DatabaseMigrator(dataSource);
        this.cleaner = new DatabaseCleaner(dataSource);
    }

    public void connect() throws SQLException {
        this.connection = dataSource.getConnection();
        this.productCatalog = new ProductsDatabase(connection);
    }

    public void clean() throws Exception {
        cleaner.clean();
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

    public void migrate() {
        migrator.migrate();
    }
}
