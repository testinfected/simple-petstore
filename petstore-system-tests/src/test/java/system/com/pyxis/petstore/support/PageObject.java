package system.com.pyxis.petstore.support;

import org.openqa.selenium.WebDriver;

public abstract class PageObject {

	protected final WebDriver webDriver;
	
	public PageObject(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public abstract void assertOnRightPage();
}
