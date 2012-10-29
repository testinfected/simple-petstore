package org.testinfected.petstore.controllers;

import com.pyxis.petstore.domain.product.Item;
import com.pyxis.petstore.domain.product.ItemNumber;
import com.pyxis.petstore.domain.product.Product;
import com.pyxis.petstore.domain.product.ProductCatalog;
import org.testinfected.petstore.Controller;
import org.testinfected.petstore.procurement.ProcurementRequestListener;

import java.math.BigDecimal;

public class CreateItem implements Controller {

    private static final int CREATED = 201;

    private final ProductCatalog productCatalog;
    private final ProcurementRequestListener requestListener;

    public CreateItem(ProductCatalog productCatalog, ProcurementRequestListener requestListener) {
        this.productCatalog = productCatalog;
        this.requestListener = requestListener;
    }

    public void process(Request request, Response response) throws Exception {
        final Product product = productCatalog.findByNumber(request.getParameter("product"));
        final Item item = new Item(new ItemNumber(request.getParameter("number")), product, new BigDecimal(request.getParameter("price")));
        item.setDescription(request.getParameter("description"));
        requestListener.addItem(item);

        response.renderHead(CREATED);
    }
}
