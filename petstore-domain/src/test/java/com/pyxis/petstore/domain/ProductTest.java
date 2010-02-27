package com.pyxis.petstore.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;


public class ProductTest {

	@Test
	public void isCreatedCorrectly()
	{
		Product product = new Product("Dog");
		assertThat(product.getName(), is("Dog"));
	}
	
}
