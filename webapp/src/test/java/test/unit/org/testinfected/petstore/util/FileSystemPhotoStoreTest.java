package test.unit.org.testinfected.petstore.util;

import org.junit.Test;
import org.testinfected.petstore.lib.FileSystemPhotoStore;
import org.testinfected.petstore.product.AttachmentStorage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FileSystemPhotoStoreTest {

    AttachmentStorage attachmentStorage = new FileSystemPhotoStore("/attachments");

    @Test public void
    photosAreStoredRelativelyToStorageRoot() {
        assertThat("photo location", attachmentStorage.getLocation("photo.png"), equalTo("/attachments/photo.png"));
    }
}
