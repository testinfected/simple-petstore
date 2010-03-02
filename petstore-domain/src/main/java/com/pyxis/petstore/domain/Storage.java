package com.pyxis.petstore.domain;

import org.springframework.stereotype.Service;

//todo refactor to photo.getLocation(storage);
@Service
public interface Storage {

    String getLocation(String attachmentUrl);
}
