package org.testinfected.petstore.helpers;

import org.testinfected.molecule.Request;
import org.testinfected.petstore.ConstraintViolation;
import org.testinfected.petstore.Validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Form<T> {

    private final String name;
    private final Map<String, Set<String>> errors = new HashMap<String, Set<String>>();
    private T value;

    public Form(String name) {
        this.name = name;
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
            rejectField(violation.path(), violation.message());
        }
        return valid();
    }

    public void reject(String errorCode) {
        addError(name, errorCode);
    }

    public void rejectField(String field, String errorCode) {
        addError(keyFor(field), errorCode);
    }

    private void addError(String key, String code) {
        errorsFor(key).add(composeMessage(key, code));
    }

    private String composeMessage(String errorKey, String errorCode) {
        return errorCode + "." + errorKey;
    }

    public boolean hasError(String key) {
        return errors.containsKey(key);
    }

    public Iterable<String> errorMessages(String key) {
        return errorsFor(key);
    }

    public boolean valid() {
        return errors.isEmpty();
    }

    private Set<String> errorsFor(String key) {
        if (!errors.containsKey(key)) errors.put(key, new HashSet<String>());
        return errors.get(key);
    }

    private String keyFor(String field) {
        return name + "." + field;
    }

    public String toString() {
        return "Form[name=" + name + ", value=" + value + ", errors=" + errors + "]";
    }
}
