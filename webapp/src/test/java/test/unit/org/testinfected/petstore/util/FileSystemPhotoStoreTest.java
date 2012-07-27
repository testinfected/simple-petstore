package test.unit.org.testinfected.petstore.util;

import com.pyxis.petstore.domain.product.AttachmentStorage;
import org.junit.Test;
import org.testinfected.petstore.util.FileSystemPhotoStore;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FileSystemPhotoStoreTest {

    AttachmentStorage attachmentStorage = new FileSystemPhotoStore("/attachments");

    @Test public void
    photosAreStoredRelativelyToStorageRoot() {
        assertThat("photo location", attachmentStorage.getLocation("photo.png"), equalTo("/attachments/photo.png"));
    }
}
