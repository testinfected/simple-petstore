package test.integration.com.pyxis.petstore.persistence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Test;

import com.pyxis.petstore.domain.PhotoAlbum;
import com.pyxis.petstore.persistence.PersistentPhotoAlbum;

public class PersistentPhotoAlbumTest {

	PhotoAlbum photoAlbum = new PersistentPhotoAlbum();
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	
	@Test
	public void showsPhoto() {
		photoAlbum.showPhoto("dalmatian", output);
		assertThat(output.size(), is(equalTo(39615)));
	}
	
}
