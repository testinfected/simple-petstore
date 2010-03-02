package com.pyxis.petstore.domain;

//todo add module dependency to spring annotations
//todo refactor to photo.getLocation(storage);
//@Service
public interface Storage {

    String getLocation(String attachmentUrl);
}
