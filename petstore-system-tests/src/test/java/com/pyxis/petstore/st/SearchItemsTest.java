package com.pyxis.petstore.st;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


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
	    driver.get("http://localhost:8080/petstore/search");
	    WebElement element = driver.findElement(By.id("searchField"));
	    assertNotNull(element);
	}
	
	@After
	public void tearDown()
	{
		driver.close();
	}

}
