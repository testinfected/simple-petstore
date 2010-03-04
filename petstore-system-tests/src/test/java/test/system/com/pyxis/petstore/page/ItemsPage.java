package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;

import test.support.com.pyxis.petstore.web.PageObject;

public class ItemsPage extends PageObject {

	public ItemsPage(WebDriver driver) {
		super(driver);
	}

	public void displaysItem(String number, String description, float price) {
	}

}
