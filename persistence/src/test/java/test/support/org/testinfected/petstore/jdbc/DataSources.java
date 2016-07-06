package test.support.org.testinfected.petstore.jdbc;

import com.vtence.tape.DriverManagerDataSource;

import javax.sql.DataSource;

public class DataSources {

    public static DataSource local() {
        return new DriverManagerDataSource(
                "jdbc:mysql://localhost:3306/petstore_test?generateSimpleParameterMetadata=true",
                "testbot",
                "petstore");
    }
}
