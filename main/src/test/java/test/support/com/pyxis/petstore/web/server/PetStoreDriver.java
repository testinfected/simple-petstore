package test.support.com.pyxis.petstore.web.server;

import org.testinfected.hamcrest.ExceptionImposter;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.util.Charsets;

public class PetStoreDriver implements ServerDriver {

    private final PetStore server;

    public PetStoreDriver(int port) {
        this.server = new PetStore(port);
    }

    public void start() {
        try {
            server.setEncoding(Charsets.UTF_8);
            server.start();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}