package com.pyxis.petstore.domain;

import org.springframework.stereotype.Service;

public @Service interface ItemNumberGenerator {

    ItemNumber nextItemNumber();
}
