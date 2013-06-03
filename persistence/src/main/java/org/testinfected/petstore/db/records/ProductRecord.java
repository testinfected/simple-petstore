package org.testinfected.petstore.db.records;

import org.testinfected.petstore.db.support.Column;
import org.testinfected.petstore.db.support.Table;
import org.testinfected.petstore.product.Attachment;
import org.testinfected.petstore.product.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testinfected.petstore.db.Access.idOf;

public class ProductRecord extends AbstractRecord<Product> {

    private final Table<Product> products = new Table<Product>("products", this);

    private final Column<Long> id = products.LONG("id");
    private final Column<String> number = products.STRING("number");
    private final Column<String> name = products.STRING("name");
    private final Column<String> description = products.STRING("description");
    private final Column<String> photo = products.STRING("photo_file_name");

    public static Table<Product> buildTable() {
        return new ProductRecord().products;
    }

    @Override
    public Product hydrate(ResultSet rs) throws SQLException {
        Product product = new Product(number.get(rs), name.get(rs));
        product.setDescription(description.get(rs));
        product.attachPhoto(new Attachment(photo.get(rs)));
        idOf(product).set(id.get(rs));
        return product;
    }

    @Override
    public void dehydrate(PreparedStatement st, Product product) throws SQLException {
        id.set(st, idOf(product).get());
        number.set(st, product.getNumber());
        name.set(st, product.getName());
        description.set(st, product.getDescription());
        photo.set(st, product.hasPhoto() ? product.getPhotoFileName() : null);
    }
}