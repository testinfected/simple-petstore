package com.pyxis.petstore.domain;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class ProductTest {

    static final String MISSING_PHOTO = "/missing.png";

    Mockery context = new JUnit4Mockery();
    Storage storage = context.mock(Storage.class);

    @Test public void
    usesDefaultPhotoIfNotSpecified() {
        Product product = aProduct().build();
        context.checking(new Expectations() {{
            oneOf(storage).getLocation(with(MISSING_PHOTO));
        }});
        product.getPhotoLocation(storage);
    }
}
