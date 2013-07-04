package org.testinfected.petstore;

import org.testinfected.molecule.Application;
import org.testinfected.molecule.Request;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.middlewares.Router;
import org.testinfected.molecule.routing.DynamicRoutes;
import org.testinfected.petstore.controllers.Checkout;
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
import org.testinfected.petstore.util.FileSystemPhotoStore;

import java.sql.Connection;

import static org.testinfected.molecule.middlewares.ConnectionScope.ConnectionReference;

public class Routing implements Application {

    private final Pages pages;

    public Routing(final Pages pages) {
        this.pages = pages;
    }

    public void handle(final Request request, final Response response) throws Exception {
        final AttachmentStorage attachmentStorage = new FileSystemPhotoStore("/photos");
        final Connection connection = new ConnectionReference(request).get();
        final Transactor transactor = new JDBCTransactor(connection);
        final ProductCatalog productCatalog = new ProductsDatabase(connection);
        final ItemInventory itemInventory = new ItemsDatabase(connection);
        final ProcurementRequestHandler requestHandler = new PurchasingAgent(productCatalog, itemInventory, transactor);
        final OrderNumberSequence orderNumberSequence = new OrderNumberDatabaseSequence(connection);
        final OrderBook orderBook = new OrdersDatabase(connection);
        final Cashier cashier = new Cashier(orderNumberSequence, orderBook, transactor);

        Router router = Router.draw(new DynamicRoutes() {{
            get("/products").to(new ListProducts(productCatalog, attachmentStorage, pages.products()));
            post("/products").to(new CreateProduct(requestHandler));
            get("/products/:product/items").to(new ListItems(itemInventory, pages.items()));
            post("/products/:product/items").to(new CreateItem(requestHandler));
            get("/cart").to(new ShowCart(pages.cart()));
            post("/cart").to(new CreateCartItem(itemInventory));
            get("/orders/new").to(new Checkout(pages.checkout()));
            get("/orders/:number").to(new ShowOrder(orderBook, pages.order()));
            post("/orders").to(new PlaceOrder(cashier, pages.checkout()));
            delete("/logout").to(new Logout());
            map("/").to(new StaticPage(pages.home()));
        }});

        router.handle(request, response);
    }
}
