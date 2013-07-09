package org.testinfected.petstore.util;

import org.testinfected.petstore.helpers.Messages;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class BundledMessages implements Messages {

    private final ResourceBundle bundle;

    public BundledMessages(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String interpolate(String error, Object... parameters) {
        MessageFormat formatter = new MessageFormat(bundle.getString(error), bundle.getLocale());
        formatter.setLocale(bundle.getLocale());
        return formatMessage(formatter, parameters);
    }

    private String formatMessage(MessageFormat formatter, Object[] parameters) {
        StringBuffer result = new StringBuffer();
        formatter.format(parameters, result, null);
        return result.toString();
    }
}
