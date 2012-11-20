package test.unit.org.testinfected.petstore.procurement;

import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.UnitOfWork;
import org.testinfected.petstore.procurement.PurchasingAgent;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static test.support.org.testinfected.petstore.builders.ItemBuilder.anItem;
import static test.support.org.testinfected.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class PurchasingAgentTest {

    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    ItemInventory itemInventory = context.mock(ItemInventory.class);
    Transactor transactor = context.mock(Transactor.class);

    PurchasingAgent purchasingAgent = new PurchasingAgent(productCatalog, itemInventory, transactor);

    States transaction = context.states("transaction").startsAs("not started");

    @Test public void
    addsNewProductToProductCatalog() throws Exception {
        context.checking(new Expectations() {{
            oneOf(transactor).perform(with(aUnitOfWork())); will(performUnitOfWork());
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
            oneOf(transactor).perform(with(aUnitOfWork())); will(performUnitOfWork());
            oneOf(itemInventory).add(with(samePropertyValuesAs(
                    anItem().of(product).withNumber("12345678").
                            describedAs("Chocolate Male").priced("599.00").build()))); when(transaction.is("started"));
        }});

        purchasingAgent.addToInventory("LAB-1234", "12345678", "Chocolate Male", new BigDecimal("599.00"));
    }

    private Matcher<UnitOfWork> aUnitOfWork() {
        return any(UnitOfWork.class);
    }

    private PerformUnitOfWork performUnitOfWork() {
        return new PerformUnitOfWork(transaction);
    }

    private static class PerformUnitOfWork implements Action {
        private final States transaction;

        public PerformUnitOfWork(States transaction) {
            this.transaction = transaction;
        }

        public Object invoke(Invocation invocation) throws Throwable {
            UnitOfWork work = (UnitOfWork) invocation.getParameter(0);
            transaction.become("started");
            work.execute();
            transaction.become("committed");
            return null;
        }

        public void describeTo(Description description) {
            description.appendText("performs unit of work");
        }
    }
}
