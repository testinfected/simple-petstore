package test.support.org.testinfected.petstore.web;

import com.vtence.molecule.helpers.Streams;
import org.hamcrest.Matcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class LogFile {

    public static LogFile create() throws IOException {
        return new LogFile(File.createTempFile("access", ".log"));
    }

    private final File file;

    public LogFile(File file) {
        this.file = file;
    }

    public void clear() {
        file.delete();
        LogManager.getLogManager().reset();
    }

    public String path() {
        return file.getAbsolutePath();
    }

    public void assertHasEntry(String text) throws IOException {
        assertHasEntry(equalTo(text));
    }

    public void assertHasEntry(Matcher<? super String> matcher) throws IOException {
        String content = Streams.toString(new FileInputStream(file));
        assertThat("log file content", content, matcher);
    }
}
