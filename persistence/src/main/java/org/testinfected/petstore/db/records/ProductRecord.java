package org.testinfected.petstore.db.records;

import com.vtence.tape.Column;
import org.testinfected.petstore.product.Attachment;
import org.testinfected.petstore.product.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.testinfected.petstore.db.Access.idOf;

public class ProductRecord extends AbstractRecord<Product> {

    private final Column<Long> id;
    private final Column<String> number;
    private final Column<String> name;
    private final Column<String> description;
    private final Column<String> photo;

    public ProductRecord(Column<Long> id,
                         Column<String> number,
                         Column<String> name,
                         Column<String> description,
                         Column<String> photo) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public Product hydrate(ResultSet rs) throws SQLException {
        Product product = new Product(number.get(rs), name.get(rs));
        product.setDescription(description.get(rs));
        product.attachPhoto(new Attachment(photo.get(rs)));
        idOf(product).set(id.get(rs));
        return product;
    }

    public void dehydrate(PreparedStatement st, Product product) throws SQLException {
        id.set(st, idOf(product).get());
        number.set(st, product.getNumber());
        name.set(st, product.getName());
        description.set(st, product.getDescription());
        photo.set(st, product.hasPhoto() ? product.getPhotoFileName() : null);
    }
}