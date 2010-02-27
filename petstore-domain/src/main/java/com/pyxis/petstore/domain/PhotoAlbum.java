package com.pyxis.petstore.domain;

import java.io.OutputStream;

public interface PhotoAlbum {

	void showPhoto(String photoKey, OutputStream outputStream);
	
}
