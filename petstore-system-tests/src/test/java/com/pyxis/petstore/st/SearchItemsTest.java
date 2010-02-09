package com.pyxis.petstore.st;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class SearchItemsTest {

	private WebDriver driver;
	
	@Before
	public void setUp()
	{
		driver = new ChromeDriver();		
	}

	@Test
	public void shouldSeeSearchPage()
	{
	    driver.get("http://localhost:8280/petstore");
	    WebElement element = driver.findElement(By.tagName("title"));
	    assertThat(element.getText(), is("PetStore"));
	}
	
	@After
	public void tearDown()
	{
		driver.close();
	}

}
