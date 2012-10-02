package test.unit.org.testinfected.petstore.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.testinfected.petstore.FileSystemResourceLoader;
import org.testinfected.petstore.Resource;
import org.testinfected.petstore.util.Streams;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.testinfected.petstore.util.Streams.toBytes;

public class FileSystemResourceLoaderTest {

    FileSystemResourceLoader resourceLoader;
    Resource resource;

    int fileSizeInBytes;
    String fileMimeType;
    long fileModificationDate;
    byte[] fileContent;

    @Before
    public void loadResourceFromFileSystemRelativeToRootDirectory() throws Exception {
        File resourceFile = locate("assets/image.png");
        fileSizeInBytes = (int) resourceFile.length();
        fileModificationDate = resourceFile.lastModified();
        fileContent = contentOf(resourceFile);
        fileMimeType = "image/png";

        File rootDirectory = resourceFile.getParentFile().getParentFile();
        resourceLoader = new FileSystemResourceLoader(rootDirectory);
        resource = resourceLoader.load("assets/image.png");
    }

    @Test public void
    givesAccessToFileSize() {
        assertThat("file size", resource.contentLength(), equalTo(fileSizeInBytes));
    }

    @Test public void
    givesAccessToFileModificationDate() {
        assertThat("file modification date", resource.lastModified(), equalTo(fileModificationDate));
    }

    @Test public void
    guessesMimeTypeFromFileExtension() {
        assertThat("file mime type", resource.mimeType(), equalTo(fileMimeType));
    }

    @Test public void
    givesAccessToFileContent() throws IOException {
        Assert.assertTrue("file content mismatch", Arrays.equals(toBytes(resource.open()), fileContent));
    }

    private File locate(final String name) throws URISyntaxException {
        URL resource = FileSystemResourceLoaderTest.class.getClassLoader().getResource(name);
        if (resource == null) throw new IllegalArgumentException("No such file: " + name);
        return new File(resource.toURI());
    }

    private byte[] contentOf(final File file) throws IOException, URISyntaxException {
        return Streams.toBytes(new FileInputStream(file));
    }
}
