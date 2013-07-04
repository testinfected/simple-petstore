package org.testinfected.petstore.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormErrors {

    private final String formName;
    private final Map<String, List<String>> messages = new HashMap<String, List<String>>();

    public FormErrors(String formName) {
        this.formName = formName;
    }

    public void reject(String errorCode) {
        addError(formName, errorCode);
    }

    public void rejectValue(String input, String errorCode) {
        addError(pathTo(input), errorCode);
    }

    private void addError(String path, String errorCode) {
        get(path).add(composeMessage(path, errorCode));
    }

    private String composeMessage(String path, String errorCode) {
        return errorCode + "." + path;
    }

    public boolean inError(String path) {
        return messages.containsKey(path);
    }

    public Iterable<String> messagesFor(String path) {
        return get(path);
    }

    public boolean empty() {
        return messages.isEmpty();
    }

    private List<String> get(String path) {
        if (!messages.containsKey(path)) messages.put(path, new ArrayList<String>());
        return messages.get(path);
    }

    private String pathTo(String field) {
        return formName + "." + field;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormErrors that = (FormErrors) o;

        if (!formName.equals(that.formName)) return false;
        if (!messages.equals(that.messages)) return false;

        return true;
    }

    public int hashCode() {
        int result = formName.hashCode();
        result = 31 * result + messages.hashCode();
        return result;
    }

    public String toString() {
        return formName + ": " + messages;
    }
}
