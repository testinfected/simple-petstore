package com.pyxis.petstore.service;

import com.pyxis.petstore.domain.AttachmentStorage;
import com.pyxis.petstore.domain.Product;
import org.springframework.stereotype.Service;

public @Service class SimplePhotoFileStore implements AttachmentStorage {
    private static final String MISSING_PHOTO = "missing.png";
    private final String rootPath;

    public SimplePhotoFileStore(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getAttachmentUrl(Product product) {
        return product.hasPhoto() ? urlOf(product.getPhotoFileName()) : urlOf(MISSING_PHOTO);
    }

    private String urlOf(String photoName) {
        return rootPath + "/" + photoName;
    }
}
