package com.pyxis.petstore.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ProductTest {

    @Test
    public void isCreatedCorrectly() {
        Product product = new Product("Dog");
        assertThat(product.getName(), is("Dog"));
    }

}
