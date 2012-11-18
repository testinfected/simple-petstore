package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Attachment;
import com.pyxis.petstore.domain.product.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.testinfected.petstore.jdbc.Properties.idOf;

public class ProductRecord extends AbstractRecord<Product> {

    public static final String TABLE = "products";

    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String PHOTO = "photo_file_name";

    @Override
    public String table() {
        return TABLE;
    }

    @Override
    public List<String> columns() {
        return Arrays.asList(ID, NUMBER, NAME, DESCRIPTION, PHOTO);
    }

    @Override
    public Product hydrate(ResultSet rs) throws SQLException {
        Product product = new Product(number(rs), name(rs));
        product.setDescription(description(rs));
        product.attachPhoto(new Attachment(photoFileName(rs)));
        idOf(product).set(id(rs));
        return product;
    }

    @Override
    public void dehydrate(PreparedStatement statement, Product product) throws SQLException {
        statement.setLong(indexOf(ID), idOf(product).get());
        statement.setString(indexOf(NUMBER), product.getNumber());
        statement.setString(indexOf(NAME), product.getName());
        statement.setString(indexOf(DESCRIPTION), product.getDescription());
        statement.setString(indexOf(PHOTO), product.hasPhoto() ? product.getPhotoFileName() : null);
    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(findColumn(rs, ID));
    }

    private String photoFileName(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, PHOTO));
    }

    private String description(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, DESCRIPTION));
    }

    private String name(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, NAME));
    }

    private String number(ResultSet rs) throws SQLException {
        return rs.getString(findColumn(rs, NUMBER));
    }
}