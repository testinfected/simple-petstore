package org.testinfected.petstore;

import com.vtence.molecule.Application;
import com.vtence.molecule.MiddlewareStack;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.middlewares.ConnectionScope;
import com.vtence.molecule.middlewares.FilterMap;
import com.vtence.molecule.middlewares.Layout;
import com.vtence.molecule.middlewares.Router;
import com.vtence.molecule.routing.DynamicRoutes;
import org.testinfected.petstore.controllers.CreateCartItem;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.controllers.ListItems;
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.controllers.Logout;
import org.testinfected.petstore.controllers.PlaceOrder;
import org.testinfected.petstore.controllers.ProceedToCheckout;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.controllers.ShowOrder;
import org.testinfected.petstore.controllers.StaticView;
import org.testinfected.petstore.db.ItemsDatabase;
import org.testinfected.petstore.db.JDBCTransactor;
import org.testinfected.petstore.db.OrderNumberDatabaseSequence;
import org.testinfected.petstore.db.OrdersDatabase;
import org.testinfected.petstore.db.ProductsDatabase;
import org.testinfected.petstore.lib.BundledMessages;
import org.testinfected.petstore.lib.FileSystemPhotoStore;
import org.testinfected.petstore.order.Cashier;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumberSequence;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.procurement.PurchasingAgent;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.transaction.Transactor;

import java.io.File;
import java.sql.Connection;
import java.util.ResourceBundle;

public class WebApp implements Application {

    private final Pages pages;
    private final PageStyles styles;

    public WebApp(final File webRoot) {
        this.pages = new Pages(new File(webRoot, "views/pages"));
        this.styles = new PageStyles(new File(webRoot, "views/layouts"));
    }

    public void handle(final Request request, final Response response) throws Exception {
        final AttachmentStorage attachments = new FileSystemPhotoStore("/photos");
        final Connection connection = new ConnectionScope.Reference(request).get();
        final Transactor transactor = new JDBCTransactor(connection);
        final ProductCatalog products = new ProductsDatabase(connection);
        final ItemInventory items = new ItemsDatabase(connection);
        final ProcurementRequestHandler procurement = new PurchasingAgent(products, items, transactor);
        final OrderNumberSequence orderNumbers = new OrderNumberDatabaseSequence(connection);
        final OrderBook orders = new OrdersDatabase(connection);
        final Cashier cashier = new Cashier(orderNumbers, orders, transactor);
        final Messages messages = new BundledMessages(ResourceBundle.getBundle("ValidationMessages"));

        Router router = Router.draw(new DynamicRoutes() {{
            get("/products").to(new ListProducts(products, attachments, pages.products()));
            post("/products").to(new CreateProduct(procurement));
            get("/products/:product/items").to(new ListItems(items, pages.items()));
            post("/products/:product/items").to(new CreateItem(procurement));
            get("/cart").to(new ShowCart(pages.cart()));
            post("/cart").to(new CreateCartItem(items));
            get("/orders/new").to(new ProceedToCheckout(pages.checkout()));
            get("/orders/:number").to(new ShowOrder(orders, pages.order()));
            post("/orders").to(new PlaceOrder(cashier, pages.checkout(), messages));
            delete("/logout").to(new Logout());
            map("/").to(new StaticView(pages.home()));
        }});

        new MiddlewareStack() {{
            use(new FilterMap().map("/", Layout.html(new PageDecorator(styles.main()))));
            run(router);
        }}.handle(request, response);
    }
}
