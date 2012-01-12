package test.support.com.pyxis.petstore.views;

import org.testinfected.hamcrest.ExceptionImposter;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.DisplayTool;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractTemplateView;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static test.support.com.pyxis.petstore.views.HTMLDocument.toElement;

public class VelocityRendering {

	private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String VELOCITY_CONFIG_FILE_URL_KEY = "velocity.config.url";
	private static final String TEMPLATES_BASE_URL_KEY = "templates.base.url";
	private static final String VIEWS_PROPERTIES_FILENAME = "/views.properties";
	private static final String VELOCITY_EXTENSION = ".vm";
    private static final String PETSTORE_MACRO_LIBRARY = "com/pyxis/petstore/helpers/petstore.vm";
    
    private VelocityEngine velocityEngine;

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final String template;
    private String encoding = DEFAULT_ENCODING;
    private String renderedView;
    private MockRequestContext mockRequestContext = new MockRequestContext();
    private Map<String, Object> model = new ExtendedModelMap();

    private VelocityRendering(String template) {
		this.template = template;
        loadVelocityEngine();
        setupTools();
        exposeRequestContext();
	}

	public static VelocityRendering render(String template) {
		return new VelocityRendering(template);
	}

	private void loadVelocityEngine() {
		try {
			VelocityConfigurer velocityConfigurer = new VelocityConfigurer() {
                @Override protected void postProcessVelocityEngine(VelocityEngine velocityEngine) {
                    super.postProcessVelocityEngine(velocityEngine);
                    velocityEngine.addProperty(
                            VelocityEngine.VM_LIBRARY, PETSTORE_MACRO_LIBRARY);
                }
            };
            velocityConfigurer.setConfigLocation(getResource(velocityPropertyFileUrl()));
			velocityConfigurer.setResourceLoaderPath(templatesBaseUrl());
            velocityConfigurer.setOverrideLogging(false);
			velocityConfigurer.afterPropertiesSet();
			velocityEngine = velocityConfigurer.getVelocityEngine();
        } catch (Exception e) {
			throw ExceptionImposter.imposterize(e);
		}
	}

    private Resource getResource(final String location) throws IOException {
        return resourceLoader.getResource(location);
    }

    private String velocityPropertyFileUrl() throws IOException {
        return getViewProperty(VELOCITY_CONFIG_FILE_URL_KEY);
    }

    private String templatesBaseUrl() throws IOException {
        return getViewProperty(TEMPLATES_BASE_URL_KEY);
    }

    private String getViewProperty(final String key) throws IOException {
        Properties properties = new Properties();
        properties.load(VelocityRendering.class.getResourceAsStream(VIEWS_PROPERTIES_FILENAME));
        return properties.getProperty(key);
    }

    public void withEncoding(String encoding) {
		if(isEmpty(encoding))
			throw new IllegalArgumentException("Invalid encoding: " + encoding);
		this.encoding = encoding;
	}

    public VelocityRendering using(ModelBuilder modelBuilder) {
        return using(modelBuilder.asMap());
    }

    public VelocityRendering bind(BindingResult result) {
        mockRequestContext.bind(result);
        return this;
    }

	public VelocityRendering using(Map<String, Object> model) {
        this.model.putAll(model);
        return this;
	}

    public String asString() {
        render();
        return renderedView;
    }

    private void render() {
        renderedView = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateFileName(), this.encoding, model);
    }

    private void exposeRequestContext() {
        model.put(AbstractTemplateView.SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE, mockRequestContext);
    }

    public Element asDom() {
        return toElement(asString());
    }

    private void setupTools() {
        model.put("base", PathFor.BASE_URL);
        model.put("display", new DisplayTool());
        model.put("date", dateTool());
    }

    private DateTool dateTool() {
        DateTool dateTool = new DateTool();
        Map<String, String> dateToolParams = new HashMap<String, String>();
        dateToolParams.put("format", "yyyy-MM-dd");
        dateTool.configure(dateToolParams);
        return dateTool;
    }

    private String templateFileName() {
		return template + VELOCITY_EXTENSION;
	}
}
