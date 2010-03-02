package test.com.pyxis.petstore.service;

import com.pyxis.petstore.domain.Storage;
import com.pyxis.petstore.service.SimpleFileStore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class SimpleFileStoreTest {

    static final String STORAGE_ROOT_PATH = "/path/to/storage";
    Storage storage = new SimpleFileStore(STORAGE_ROOT_PATH);

    @Test public void
    attachmentIsStoredRelativelyToStorageRoot() {
        String relativeUrl = "/relative/path/to/attachment";
        assertThat(storage.getLocation(relativeUrl), equalTo(absoluteUrl(relativeUrl)));
    }

    private String absoluteUrl(String relativeUrl) {
        return STORAGE_ROOT_PATH + relativeUrl;
    }
}
