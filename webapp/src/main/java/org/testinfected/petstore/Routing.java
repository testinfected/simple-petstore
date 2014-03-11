package org.testinfected.petstore;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.Router;
import org.testinfected.molecule.routing.DynamicRoutes;
import org.testinfected.petstore.controllers.ProceedToCheckout;
import org.testinfected.petstore.controllers.CreateCartItem;
import org.testinfected.petstore.controllers.CreateItem;
import org.testinfected.petstore.controllers.CreateProduct;
import org.testinfected.petstore.controllers.ListItems;
import org.testinfected.petstore.controllers.ListProducts;
import org.testinfected.petstore.controllers.Logout;
import org.testinfected.petstore.controllers.PlaceOrder;
import org.testinfected.petstore.controllers.ShowCart;
import org.testinfected.petstore.controllers.ShowOrder;
import org.testinfected.petstore.controllers.StaticPage;
import org.testinfected.petstore.db.ItemsDatabase;
import org.testinfected.petstore.db.JDBCTransactor;
import org.testinfected.petstore.db.OrderNumberDatabaseSequence;
import org.testinfected.petstore.db.OrdersDatabase;
import org.testinfected.petstore.db.ProductsDatabase;
import org.testinfected.petstore.order.Cashier;
import org.testinfected.petstore.order.OrderBook;
import org.testinfected.petstore.order.OrderNumberSequence;
import org.testinfected.petstore.procurement.ProcurementRequestHandler;
import org.testinfected.petstore.procurement.PurchasingAgent;
import org.testinfected.petstore.product.AttachmentStorage;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.transaction.Transactor;
import org.testinfected.petstore.util.BundledMessages;
import org.testinfected.petstore.util.FileSystemPhotoStore;

import java.sql.Connection;
import java.util.ResourceBundle;

import static org.testinfected.molecule.middlewares.ConnectionScope.ConnectionReference;

public class Routing implements Application {

    private final Pages pages;

    public Routing(final Pages pages) {
        this.pages = pages;
    }

    public void handle(final Request request, final Response response) throws Exception {
        final AttachmentStorage attachments = new FileSystemPhotoStore("/photos");
        final Connection connection = new ConnectionReference(request).get();
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
            map("/").to(new StaticPage(pages.home()));
        }});

        router.handle(request, response);
    }
}
