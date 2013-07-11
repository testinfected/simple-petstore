package org.testinfected.petstore.procurement;

import org.testinfected.petstore.product.Attachment;
import org.testinfected.petstore.product.Item;
import org.testinfected.petstore.product.ItemInventory;
import org.testinfected.petstore.product.ItemNumber;
import org.testinfected.petstore.product.Product;
import org.testinfected.petstore.product.ProductCatalog;
import org.testinfected.petstore.transaction.Transactor;
import org.testinfected.petstore.transaction.UnitOfWork;

import java.math.BigDecimal;

public class PurchasingAgent implements ProcurementRequestHandler {
    private final ProductCatalog productCatalog;
    private final ItemInventory itemInventory;
    private final Transactor transactor;

    public PurchasingAgent(ProductCatalog productCatalog, ItemInventory itemInventory, Transactor transactor) {
        this.productCatalog = productCatalog;
        this.itemInventory = itemInventory;
        this.transactor = transactor;
    }

    public void addProductToCatalog(String number, String name, String description, String photoFileName) throws Exception {
        final Product product = new Product(number, name);
        product.description(description);
        product.attachPhoto(new Attachment(photoFileName));

        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                productCatalog.add(product);
            }
        });
    }

    public void addToInventory(String productNumber, String itemNumber, String description, BigDecimal price) throws Exception {
        final Product product = productCatalog.findByNumber(productNumber);
        final Item item = new Item(new ItemNumber(itemNumber), product, price);
        item.description(description);

        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                itemInventory.add(item);
            }
        });
    }
}
