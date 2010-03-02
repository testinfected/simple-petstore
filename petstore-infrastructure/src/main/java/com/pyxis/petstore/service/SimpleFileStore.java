package com.pyxis.petstore.service;

import com.pyxis.petstore.domain.Storage;

public class SimpleFileStore implements Storage {
    private final String rootPath;

    public SimpleFileStore(String rootPath) {
        this.rootPath = rootPath;
    }

    public String getLocation(String attachmentUrl) {
        return rootPath + attachmentUrl;
    }
}
