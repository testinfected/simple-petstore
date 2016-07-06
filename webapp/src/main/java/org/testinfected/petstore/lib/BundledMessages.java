package org.testinfected.petstore.lib;

import org.testinfected.petstore.Messages;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class BundledMessages  implements Messages {

    private final ResourceBundle bundle;

    public BundledMessages(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public String interpolate(String error, Object... parameters) {
        MessageFormat formatter = new MessageFormat(bundle.getString(error), bundle.getLocale());
        return formatMessage(formatter, parameters);
    }

    private String formatMessage(MessageFormat formatter, Object[] parameters) {
        StringBuffer result = new StringBuffer();
        return formatter.format(parameters, result, null).toString();
    }
}
