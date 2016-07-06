package test.support.org.testinfected.petstore.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializedForm<T> extends TypeSafeDiagnosingMatcher<T> {

    private final Matcher<? super T> cloneMatcher;

    public SerializedForm(Matcher<? super T> cloneMatcher) {
        this.cloneMatcher = cloneMatcher;
    }

    protected boolean matchesSafely(T original, Description mismatchDescription) {
        boolean matches;
        try {
            matches = cloneMatcher.matches(serializedClone(original));
        } catch (ClassNotFoundException | IOException e) {
            mismatchDescription.appendText("not serializable " + e.getMessage());
            return false;
        }

        if (!matches) {
            mismatchDescription.appendText("serialized form ");
            cloneMatcher.describeMismatch(original, mismatchDescription);
        }

        return matches;

    }

    private T serializedClone(T item) throws IOException, ClassNotFoundException {
        byte[] pickled = serialize(item);
        return deserialize(pickled);
    }

    @SuppressWarnings("unchecked")
    private T deserialize(byte[] pickled) throws IOException, ClassNotFoundException {
        InputStream in = new ByteArrayInputStream(pickled);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(in);
            return (T) ois.readObject();
        } finally {
            close(ois);
        }
    }

    private byte[] serialize(T item) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeObject(item);
        } finally {
            close(oos);
        }

        return out.toByteArray();
    }

    private void close(Closeable stream) {
        try {
            if (stream != null) stream.close();
        } catch (IOException ignored) {
        }
    }

    public void describeTo(Description description) {
        description.appendText("with serialized form ").appendDescriptionOf(cloneMatcher);
    }

    public static <T> SerializedForm<T> serializedForm(Matcher<? super T> cloneMatcher) {
        return new SerializedForm<>(cloneMatcher);
    }
}
