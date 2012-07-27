package org.testinfected.petstore.util;

import com.pyxis.petstore.domain.product.AttachmentStorage;

public class FileSystemPhotoStore implements AttachmentStorage {

    private final String rootPath;

    public FileSystemPhotoStore(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getLocation(String fileName) {
        return uriOf(fileName);
    }

    private String uriOf(String photoName) {
        return rootPath + "/" + photoName;
    }
}

