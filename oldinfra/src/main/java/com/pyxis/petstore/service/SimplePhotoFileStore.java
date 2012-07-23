package com.pyxis.petstore.service;

import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.domain.product.Product;
import org.springframework.stereotype.Service;

@Service
public class SimplePhotoFileStore implements AttachmentStorage {
    private static final String MISSING_PHOTO = "missing.png";
    private final String rootPath;

    public SimplePhotoFileStore(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getAttachmentUri(Product product) {
        return product.hasPhoto() ? uriOf(product.getPhotoFileName()) : uriOf(MISSING_PHOTO);
    }

    private String uriOf(String photoName) {
        return rootPath + "/" + photoName;
    }
}
