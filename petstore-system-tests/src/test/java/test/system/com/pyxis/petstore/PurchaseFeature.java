package test.system.com.pyxis.petstore;

import com.pyxis.petstore.domain.product.Product;
import org.junit.Before;
import org.junit.Test;

import static test.support.com.pyxis.petstore.builders.ItemBuilder.a;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class PurchaseFeature extends FeatureTemplate {

    @Before public void
    labradorsAreForSale() {
        Product labrador = aProduct().withName("Labrador Retriever").build();
        Product golden = aProduct().withName("Golden Retriever").build();
        legacyContext.given(labrador, golden);
        legacyContext.given(
                a(labrador).withNumber("11111111").describedAs("Male Adult").priced("599.00"),
                a(golden).withNumber("22222222").describedAs("Female Adult").priced("649.00"));
    }

    @Test public void
    purchasesSeveralItemsUsingACreditCard() {
        legacyPetstore.buy("Labrador Retriever", "11111111");
        legacyPetstore.buy("Golden Retriever", "22222222");
        legacyPetstore.checkout();
        legacyPetstore.showsTotalToPay("1248.00");

        legacyPetstore.pay("John", "Leclair", "jleclair@gmail.com", "Visa", "9999 9999 9999 9999", "12/12");
        legacyPetstore.showsTotalPaid("1248.00");
        legacyPetstore.showsLineItem("11111111", "Male Adult", "599.00");
        legacyPetstore.showsLineItem("22222222", "Female Adult", "649.00");
        legacyPetstore.showsBillingInformation("John", "Leclair", "jleclair@gmail.com");
        legacyPetstore.showsCreditCardDetails("Visa", "9999 9999 9999 9999", "12/12");

        legacyPetstore.continueShopping();
        legacyPetstore.showsCartIsEmpty();
    }
}
