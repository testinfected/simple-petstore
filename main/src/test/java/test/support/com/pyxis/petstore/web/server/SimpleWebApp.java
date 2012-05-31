package test.support.com.pyxis.petstore.web.server;

import org.testinfected.hamcrest.ExceptionImposter;
import org.testinfected.petstore.PetStore;

public class SimpleWebApp implements ServerDriver {

    final PetStore petstore;

    public SimpleWebApp(int port) {
        petstore = new PetStore(port);
    }

    public void start() {
        try {
            petstore.start();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }

    public void stop() {
        try {
            petstore.stop();
        } catch (Exception e) {
            throw ExceptionImposter.imposterize(e);
        }
    }
}