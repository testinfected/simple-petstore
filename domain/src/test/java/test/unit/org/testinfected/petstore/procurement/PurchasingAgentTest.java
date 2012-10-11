package test.unit.org.testinfected.petstore.procurement;

import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.UnitOfWork;
import org.testinfected.petstore.procurement.PurchasingAgent;

import static org.jmock.Expectations.any;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

@RunWith(JMock.class)
public class PurchasingAgentTest {

    Mockery context = new JUnit4Mockery();
    ProductCatalog productCatalog = context.mock(ProductCatalog.class);
    Transactor transactor = context.mock(Transactor.class);
    PurchasingAgent purchasingAgent = new PurchasingAgent(productCatalog, transactor);

    @Test public void
    addsNewProductToProductCatalog() throws Exception {
        final Product product = aProduct().build();

        context.checking(new Expectations() {{
            oneOf(transactor).perform(with(aUnitOfWork())); will(performUnitOfWork());
            oneOf(productCatalog).add(with(same(product)));
        }});

        purchasingAgent.addProduct(product);
    }

    private Matcher<UnitOfWork> aUnitOfWork() {
        return any(UnitOfWork.class);
    }

    private PerformUnitOfWork performUnitOfWork() {
        return new PerformUnitOfWork();
    }

    private static class PerformUnitOfWork implements Action {
        public Object invoke(Invocation invocation) throws Throwable {
            UnitOfWork work = (UnitOfWork) invocation.getParameter(0);
            work.execute();
            return null;
        }

        public void describeTo(Description description) {
            description.appendText("performs unit of work");
        }
    }
}
