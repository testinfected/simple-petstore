package org.testinfected.petstore.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FormErrors {

    private final String formName;
    private final Map<String, Set<String>> messages = new HashMap<String, Set<String>>();

    public FormErrors(String formName) {
        this.formName = formName;
    }

    public void reject(String errorCode) {
        addError(formName, errorCode);
    }

    public void rejectValue(String field, String errorCode) {
        addError(keyFor(field), errorCode);
    }

    private void addError(String key, String errorCode) {
        get(key).add(composeMessage(key, errorCode));
    }

    private String composeMessage(String key, String errorCode) {
        return errorCode + "." + key;
    }

    public boolean hasError(String key) {
        return messages.containsKey(key);
    }

    public Iterable<String> messagesFor(String key) {
        return get(key);
    }

    public boolean empty() {
        return messages.isEmpty();
    }

    private Set<String> get(String path) {
        if (!messages.containsKey(path)) messages.put(path, new HashSet<String>());
        return messages.get(path);
    }

    private String keyFor(String field) {
        return formName + "." + field;
    }
}
