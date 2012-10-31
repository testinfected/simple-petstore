package org.testinfected.petstore.procurement;

import java.math.BigDecimal;

public interface ProcurementRequestHandler {

    void addProductToCatalog(String number, String name, String description, String photoFileName) throws Exception;

    void addToInventory(String productNumber, String itemNumber, String description, BigDecimal price) throws Exception;
}
