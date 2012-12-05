package org.testinfected.support.decoration;

import org.testinfected.support.HttpStatus;
import org.testinfected.support.Response;
import org.testinfected.support.util.MimeTypes;

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
