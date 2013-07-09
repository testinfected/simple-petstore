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
    private Messages messages = new Messages() {
        public String interpolate(String error, Object... parameters) {
            return error;
        }
    };
    private T value;

    public Form(String name) {
        this.name = name;
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

    public void reject(String errorCode) {
        addError(name, errorCode);
    }

    public void rejectField(String field, String errorCode) {
        addError(errorKey(field), errorCode);
    }

    private String errorKey(String field) {
        return name + "." + field;
    }

    private void addError(String errorKey, String errorCode) {
        addErrorMessage(errorKey, message(errorKey, errorCode));
    }

    private void addErrorMessage(String errorKey, String message) {
        errorsFor(errorKey).add(message);
    }

    private String message(String errorKey, String errorCode) {
        return messages.interpolate(errorCode + "." + errorKey);
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

    public String toString() {
        return "Form[name=" + name + ", value=" + value + ", errors=" + errors + "]";
    }
}
