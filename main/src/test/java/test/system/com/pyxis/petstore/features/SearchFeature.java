package test.system.com.pyxis.petstore.features;

import com.pyxis.petstore.domain.product.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import test.support.com.pyxis.petstore.builders.ProductBuilder;
import test.support.com.pyxis.petstore.web.ApplicationDriver;
import test.support.com.pyxis.petstore.web.SystemTestContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;
import static test.support.com.pyxis.petstore.web.SystemTestContext.systemTesting;

public class SearchFeature {

    SystemTestContext context = systemTesting();
    ApplicationDriver application;

    @Before public void
    startApplication() {
        application = context.startApplication();
    }

    @After public void
    stopApplication() {
        context.stopApplication(application);
    }

    @Test public void
    searchesForAProductNotAvailableInStore() {
        context.given(aProduct().named("Labrador Retriever"));

        application.searchFor("Dalmatian");
        application.showsNoResult();
    }

    @Test public void
    searchesAndFindsProductsInCatalog() throws SQLException {
        givenProducts(aProduct("LAB-1234").named("Labrador Retriever"),
                aProduct("CHE-5678").named("Chesapeake").describedAs("Chesapeake bay retriever"),
                aProduct().named("Dalmatian"));

        application.searchFor("retriever");
        application.displaysNumberOfResults(2);
        application.displaysProduct("LAB-1234", "Labrador Retriever");
        application.displaysProduct("CHE-5678", "Chesapeake");
    }
                                
    private void givenProducts(ProductBuilder... builders) throws SQLException {
        for (ProductBuilder builder : builders) {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/petstore-test", "testbot", "petstore");
            try {
                addProductToCatalog(connection, builder.build());
            } finally {
                connection.close();
            }
        }
    }

    private void addProductToCatalog(Connection connection, Product product) throws SQLException {
        PreparedStatement st = connection.prepareStatement("insert into products(name, description, photo_file_name, number) values(?, ?, ?, ?);");
        st.setString(1, product.getName());
        st.setString(2, product.getDescription());
        st.setString(3, product.getPhotoFileName());
        st.setString(4, product.getNumber());
        assertThat("rows inserted", st.executeUpdate(), equalTo(1));
        st.close();
    }
}
