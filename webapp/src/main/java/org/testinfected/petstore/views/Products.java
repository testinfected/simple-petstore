package org.testinfected.petstore.views;

import com.samskivert.mustache.Mustache;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.Product;

import java.util.ArrayList;
import java.util.Collection;

public class Products {
    private final Collection<Product> products = new ArrayList<>();
    private String keyword;
    private AttachmentStorage photos;

    public Products matching(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public Products add(Collection<Product> found) {
        products.addAll(found);
        return this;
    }

    public Iterable<Product> getEach() {
        return products;
    }

    public int getCount() {
        return products.size();
    }

    public boolean getNone() {
        return products.isEmpty();
    }

    public Products withPhotosIn(AttachmentStorage storage) {
        this.photos = storage;
        return this;
    }

    public AttachmentStorage getPhotos() {
        return photos;
    }

    public Mustache.Lambda getPhotoUrl() {
        return (frag, out) -> out.write(photos.getLocation(frag.execute()));
    }
}
