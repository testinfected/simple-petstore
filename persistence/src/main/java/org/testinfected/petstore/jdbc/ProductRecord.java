package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Attachment;
import com.pyxis.petstore.domain.product.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testinfected.petstore.jdbc.Properties.idOf;

public class ProductRecord implements Record<Product> {

    public Product hydrate(ResultSet resultSet) throws SQLException {
        Product product = new Product(resultSet.getString("number"), resultSet.getString("name"));
        product.setDescription(resultSet.getString("description"));
        String photoFileName = resultSet.getString("photo_file_name");
        product.attachPhoto(new Attachment(photoFileName));

        idOf(product).set(resultSet.getLong("id"));
        return product;
    }
}