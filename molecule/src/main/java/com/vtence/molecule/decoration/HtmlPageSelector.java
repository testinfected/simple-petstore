package com.vtence.molecule.decoration;

import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.Response;
import com.vtence.molecule.http.MimeTypes;

public class HtmlPageSelector implements Selector {

    public boolean selected(Response response) {
        return isOk(response.statusCode()) && isHtml(response.contentType());
    }

    private boolean isOk(int code) {
        return code == HttpStatus.OK.code;
    }

    private boolean isHtml(String contentType) {
        return contentType != null && contentType.startsWith(MimeTypes.HTML);
    }
}
