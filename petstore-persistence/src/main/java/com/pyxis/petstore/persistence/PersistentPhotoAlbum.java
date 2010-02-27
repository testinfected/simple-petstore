package com.pyxis.petstore.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.pyxis.petstore.domain.PhotoAlbum;

public class PersistentPhotoAlbum implements PhotoAlbum {

	private static final String EXTENSION = ".jpg";
	private static final String PHOTOS_BASE_DIRECTORY = "/photos/";

	public void showPhoto(String photoKey, OutputStream display) {
		try {
			IOUtils.copy(getPhotoData(photoKey), display);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private InputStream getPhotoData(String photoKey) {
		return getClass().getResourceAsStream(photoLocation(photoKey));
	}

	private String photoLocation(String photoKey) {
		return PHOTOS_BASE_DIRECTORY + photoKey + EXTENSION;
	}

}
