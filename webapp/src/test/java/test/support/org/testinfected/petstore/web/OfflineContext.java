package test.support.org.testinfected.petstore.web;

import org.testinfected.petstore.FileSystemResourceLoader;
import org.testinfected.petstore.MustacheRendering;
import org.testinfected.petstore.PetStore;
import org.testinfected.petstore.Renderer;
import org.testinfected.petstore.util.Charsets;
import test.support.com.pyxis.petstore.Properties;

import java.io.File;

public class OfflineContext {
    
    private static final Properties defaults = new Properties();

    static {
        defaults.put("web.root", "webapp/src/main/webapp");
    }

    private final Properties properties;

    public static OfflineContext offlineContext() {
        return new OfflineContext(Properties.system());
    }

    public OfflineContext(Properties properties) {
        this.properties = defaults.copy();
        this.properties.override(properties);
    }

    public File webRoot() {
        return properties.getFile("web.root");
    }

    public Renderer renderer() {
        return new MustacheRendering(new FileSystemResourceLoader(new File(webRoot(), PetStore.TEMPLATE_DIRECTORY), Charsets.UTF_8));
    }


}
