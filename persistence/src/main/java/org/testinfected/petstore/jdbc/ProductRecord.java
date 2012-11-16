package org.testinfected.petstore.jdbc;

import com.pyxis.petstore.domain.product.Attachment;
import com.pyxis.petstore.domain.product.Product;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.testinfected.petstore.jdbc.Properties.idOf;

// todo: Eliminate duplication in record classes
public class ProductRecord implements Record<Product> {

    private static final String PRODUCTS_TABLE = "products";

    public Product hydrate(ResultSet rs) throws SQLException {
        Product product = new Product(number(rs), name(rs));
        product.setDescription(description(rs));
        product.attachPhoto(new Attachment(photoFileName(rs)));
        idOf(product).set(id(rs));
        return product;
    }

    private long id(ResultSet rs) throws SQLException {
        return rs.getLong(getColumnIndex(rs, "id"));
    }

    private String photoFileName(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "photo_file_name"));
    }

    private String description(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "description"));
    }

    private String name(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "name"));
    }

    private String number(ResultSet rs) throws SQLException {
        return rs.getString(getColumnIndex(rs, "number"));
    }

    private int getColumnIndex(ResultSet rs, final String columnName) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            if (metaData.getColumnName(i).equalsIgnoreCase(columnName) &&
                    metaData.getTableName(i).equalsIgnoreCase(PRODUCTS_TABLE))
                return i;
        }

        throw new SQLException("Result set has no column '" + columnName + "'");
    }

    public void dehydrate(Row row, Product product) {
        row.setValue("number", product.getNumber());
        row.setValue("name", product.getName());
        row.setValue("description", product.getDescription());
        row.setValue("photo", product.hasPhoto() ? product.getPhotoFileName() : null);
    }
}