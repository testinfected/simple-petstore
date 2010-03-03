package test.com.pyxis.petstore.service;

import com.pyxis.petstore.domain.AttachmentStorage;
import com.pyxis.petstore.domain.Product;
import com.pyxis.petstore.service.SimplePhotoFileStore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static test.support.com.pyxis.petstore.builders.ProductBuilder.aProduct;

public class SimplePhotoFileStoreTest {

    static final String STORAGE_ROOT_PATH = "/path/to/attachmentStore";
    static final String DEFAULT_PHOTO = "missing.png";
    AttachmentStorage attachmentStorage = new SimplePhotoFileStore(STORAGE_ROOT_PATH);

    @Test public void
    photoIsStoredRelativelyToStorageRoot() {
        Product withPhoto = aProduct().withPhoto("photo.png").build();
        assertThat(attachmentStorage.getAttachmentUrl(withPhoto), equalTo(urlOf(withPhoto.getPhotoName())));
    }

    @Test public void
    defaultPhotoIsUsedWhenProductHasNone() {
        Product noPhoto = aProduct().withNoPhoto().build();
        assertThat(attachmentStorage.getAttachmentUrl(noPhoto), equalTo(urlOf(DEFAULT_PHOTO)));
    }

    private String urlOf(String photoName) {
        return STORAGE_ROOT_PATH + "/" + photoName;
    }
}
