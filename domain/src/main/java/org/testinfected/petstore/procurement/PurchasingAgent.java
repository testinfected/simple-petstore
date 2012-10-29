package org.testinfected.petstore.procurement;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemInventory;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.Transactor;
import org.testinfected.petstore.UnitOfWork;

public class PurchasingAgent implements ProcurementRequestListener {
    private final ProductCatalog productCatalog;
    private final ItemInventory itemInventory;
    private final Transactor transactor;

    public PurchasingAgent(ProductCatalog productCatalog, ItemInventory itemInventory, Transactor transactor) {
        this.productCatalog = productCatalog;
        this.itemInventory = itemInventory;
        this.transactor = transactor;
    }

    public void addProduct(final Product product) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                productCatalog.add(product);
            }
        });
    }

    public void addItem(final Item item) throws Exception {
        transactor.perform(new UnitOfWork() {
            public void execute() throws Exception {
                itemInventory.add(item);
            }
        });
    }
}
