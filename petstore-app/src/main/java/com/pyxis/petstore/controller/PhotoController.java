package com.pyxis.petstore.controller;

import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pyxis.petstore.domain.PhotoAlbum;

@Controller
@RequestMapping("/photos")
public class PhotoController {

	private final PhotoAlbum photoAlbum;
	
	@Autowired
	public PhotoController(PhotoAlbum photoAlbum) {
		this.photoAlbum = photoAlbum;
	}

	@RequestMapping("/{photoKey}")
	public void streamImageContent(@PathVariable("photoKey") String photoKey, OutputStream output) {
		this.photoAlbum.showPhoto(photoKey, output);
	}
}
