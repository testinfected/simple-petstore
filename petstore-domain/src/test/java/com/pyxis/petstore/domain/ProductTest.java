package com.pyxis.petstore.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ProductTest {

    @Test public void
    isCreatedCorrectly() {
        Product product = new Product("Dog");
        assertThat(product.getName(), is("Dog"));
    }

    @Test public void
    usesDefaultPhotoUnlessSpecified() {
        Product product = new Product("a product");
        assertThat(product.getPhotoUrl(), equalTo("/missing.png"));
        product.setPhotoUrl("/labrador.png");
        assertThat(product.getPhotoUrl(), equalTo("/labrador.png"));
    }
}
