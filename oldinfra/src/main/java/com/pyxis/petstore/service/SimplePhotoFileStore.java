package com.pyxis.petstore.service;

import com.pyxis.petstore.domain.product.AttachmentStorage;
import org.springframework.stereotype.Service;

@Service
public class SimplePhotoFileStore implements AttachmentStorage {

    private final String rootPath;

    public SimplePhotoFileStore(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getLocation(String fileName) {
        return uriOf(fileName);
    }

    private String uriOf(String photoName) {
        return rootPath + "/" + photoName;
    }
}
