package org.testinfected.petstore.helpers;

import org.testinfected.molecule.Request;
import org.testinfected.petstore.validation.ConstraintViolation;
import org.testinfected.petstore.validation.Path;
import org.testinfected.petstore.validation.Validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Form<T> {

    private final Path root;
    private final Map<String, Set<String>> errors = new HashMap<String, Set<String>>();
    private Messages messages = new Messages() {
        public String interpolate(String error, Object... parameters) {
            return error;
        }
    };
    private T value;

    public Form(String name) {
        this.root = Path.root(this).node(name);
    }

    public Form<T> use(Messages messages) {
        this.messages = messages;
        return this;
    }

    protected abstract T parse(Request request);

    public T value() {
        return value;
    }

    public void load(Request request) {
        this.value = parse(request);
    }

    public boolean validate(Validator validator) {
        Set<ConstraintViolation<?>> violations = validator.validate(value());
        for (ConstraintViolation<?> violation : violations) {
            rejectField(violation.path(), violation.error());
        }
        return valid();
    }

    public void reject(String error) {
        addError(root, error);
    }

    public void rejectField(String field, String error) {
        addError(root.node(field), error);
    }

    private void addError(Path path, String error) {
        addErrorMessage(path.value(), translate(path.value(), error));
    }

    private void addErrorMessage(String path, String message) {
        errorsOn(path).add(message);
    }

    private String translate(String path, String error) {
        return messages.interpolate(error + "." + path);
    }

    public boolean hasError(String key) {
        return errors.containsKey(key);
    }

    public Iterable<String> errorMessages(String key) {
        return errorsOn(key);
    }

    public boolean valid() {
        return errors.isEmpty();
    }

    private Set<String> errorsOn(String key) {
        if (!errors.containsKey(key)) errors.put(key, new HashSet<String>());
        return errors.get(key);
    }

    public String toString() {
        return "Form[name=" + root.name() + ", value=" + value + ", errors=" + errors + "]";
    }
}
