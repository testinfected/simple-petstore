package org.testinfected.petstore.procurement;

import com.pyxis.petstore.domain.product.Attachment;
import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.ItemNumber;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.UnitOfWork;

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
        product.setDescription(description);
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
        item.setDescription(description);

        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                itemInventory.add(item);
            }
        });

    }
}
