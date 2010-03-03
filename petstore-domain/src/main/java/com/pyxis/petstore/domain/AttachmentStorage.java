package com.pyxis.petstore.domain;

import org.springframework.stereotype.Service;

@Service
public interface AttachmentStorage {

    String getAttachmentUrl(Product product);
}
