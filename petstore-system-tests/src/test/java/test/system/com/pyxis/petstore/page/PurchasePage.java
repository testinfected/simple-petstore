package test.system.com.pyxis.petstore.page;

import org.openqa.selenium.WebDriver;
import test.support.com.pyxis.petstore.web.PageObject;

import java.math.BigDecimal;

public class PurchasePage extends PageObject {

    public PurchasePage(WebDriver driver) {
        super(driver);
    }

    public void showsTotalToPay(BigDecimal total) {
    }
}
