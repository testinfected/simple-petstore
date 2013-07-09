package test.unit.org.testinfected.petstore.procurement;

import org.jmock.Expectations;
import org.jmock.States;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import org.testinfected.petstore.procurement.PurchasingAgent;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.transaction.AbstractTransactor;
import org.testinfected.petstore.transaction.UnitOfWork;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

public class PurchasingAgentTest {
    @Rule public JUnitRuleMockery context = new JUnitRuleMockery();

    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    ItemInventory itemInventory = context.mock(ItemInventory.class);

    States transaction = context.states("transaction").startsAs("not started");
    PurchasingAgent purchasingAgent = new PurchasingAgent(productCatalog, itemInventory, new StubTransactor(transaction));

    @Test public void
    addsNewProductToProductCatalog() throws Exception {
        context.checking(new Expectations() {{
            oneOf(productCatalog).add(with(samePropertyValuesAs(
                    aProduct("LAB-1234").named("Labrador").
                            describedAs("Friendly Dog").withPhoto("Labrador.jpg").build()))); when(transaction.is("started"));
        }});

        purchasingAgent.addProductToCatalog("LAB-1234", "Labrador", "Friendly Dog", "Labrador.jpg");
    }

    @Test public void
    addsNewItemsToInventory() throws Exception {
        final Product product = aProduct().withNumber("LAB-1234").build();
        context.checking(new Expectations() {{
            allowing(productCatalog).findByNumber(with("LAB-1234")); will(returnValue(product));
            oneOf(itemInventory).add(with(samePropertyValuesAs(
                    anItem().of(product).withNumber("12345678").
                            describedAs("Chocolate Male").priced("599.00").build()))); when(transaction.is("started"));
        }});

        purchasingAgent.addToInventory("LAB-1234", "12345678", "Chocolate Male", new BigDecimal("599.00"));
    }

    private class StubTransactor extends AbstractTransactor {
        private final States transaction;

        public StubTransactor(States transaction) {
            this.transaction = transaction;
        }

        public void perform(UnitOfWork work) throws Exception {
            transaction.become("started");
            work.execute();
            transaction.become("committed");
        }
    }
}
