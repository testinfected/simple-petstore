package org.testinfected.molecule.decoration;

import org.testinfected.molecule.HttpStatus;
import org.testinfected.molecule.Response;
import org.testinfected.molecule.util.MimeTypes;

public class HtmlPageSelector implements Selector {

    public boolean select(Response response) {
        return isOk(response.statusCode()) && isHtml(response.contentType());
    }

    private boolean isOk(int code) {
        return code == HttpStatus.OK.code;
    }

    private boolean isHtml(String contentType) {
        return contentType != null && contentType.startsWith(MimeTypes.TEXT_HTML);
    }
}
