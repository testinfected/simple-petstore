package org.testinfected.petstore.helpers;

import org.testinfected.petstore.validation.ConstraintViolation;
import org.testinfected.petstore.validation.Validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Form {

    private final Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();

    public boolean validate(Validator validator) {
        violations.addAll(validator.validate(this));
        return violations.isEmpty();
    }

    public Form.Errors errors(Messages messages) {
        Form.Errors errors = new Form.Errors();
        for (ConstraintViolation<?> violation : violations) {
            errors.add(violation.path(), translate(messages, violation.path(), violation.error()));
        }
        return errors;
    }

    private String translate(Messages messages, String path, String error) {
        return messages.interpolate(error + "." + path);
    }

    public static class Errors {

        private final Map<String, Set<String>> messages = new HashMap<String, Set<String>>();

        public void add(String path, String message) {
            errorsOn(path).add(message);
        }

        public boolean contains(String key) {
            return messages.containsKey(key);
        }

        public Iterable<String> messages(String path) {
            return errorsOn(path);
        }

        public boolean empty() {
            return messages.isEmpty();
        }

        private Set<String> errorsOn(String path) {
            if (!messages.containsKey(path)) messages.put(path, new HashSet<String>());
            return messages.get(path);
        }

        public String toString() {
            return messages.toString();
        }
    }
}
