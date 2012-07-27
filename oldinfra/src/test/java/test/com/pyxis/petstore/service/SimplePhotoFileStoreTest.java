package test.com.pyxis.petstore.service;

import com.pyxis.petstore.domain.product.AttachmentStorage;
import com.pyxis.petstore.service.SimplePhotoFileStore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SimplePhotoFileStoreTest {

    AttachmentStorage attachmentStorage = new SimplePhotoFileStore("/photos");

    @Test public void
    photosAreStoredRelativeToStorageRoot() {
        assertThat("attachment url", attachmentStorage.getLocation("photo.png"), equalTo("/photos/photo.png"));
    }
}
