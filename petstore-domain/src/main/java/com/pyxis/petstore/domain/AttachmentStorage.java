package com.pyxis.petstore.domain;

//todo add module dependency to spring annotations
//todo refactor to photo.getLocation(storage);
//@Service
public interface AttachmentStorage {

    String getLocation(String attachmentUri);
}
