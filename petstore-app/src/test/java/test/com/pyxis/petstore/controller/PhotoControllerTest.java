package test.com.pyxis.petstore.controller;

import java.io.OutputStream;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.pyxis.petstore.controller.PhotoController;
import com.pyxis.petstore.domain.PhotoAlbum;

@RunWith(JMock.class)
public class PhotoControllerTest {

    Mockery context = new JUnit4Mockery();
    OutputStream outputStream = new ByteArrayOutputStream();
	PhotoAlbum photoAlbum = context.mock(PhotoAlbum.class);
	PhotoController photoController = new PhotoController(photoAlbum);
	
	@Test
	public void showsPhotoFromPhotoAlbum() {
		context.checking(new Expectations(){{
			oneOf(photoAlbum).showPhoto("somePhotoKey", outputStream);
		}});
		photoController.showPhoto("somePhotoKey", outputStream);
	}
	
}
