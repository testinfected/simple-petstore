package com.pyxis.petstore.domain;

import org.springframework.stereotype.Service;

@Service
public interface Storage {

    String getLocation(String attachmentUrl);
}
