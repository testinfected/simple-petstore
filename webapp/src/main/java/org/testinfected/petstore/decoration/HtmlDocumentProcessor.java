package org.testinfected.petstore.decoration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlDocumentProcessor implements ContentProcessor {

    private final Pattern head = Pattern.compile(".*<head>(.*)</head>.*", Pattern.DOTALL);
    private final Pattern title = Pattern.compile(".*<head>.*<title>(.*)</title>.*</head>.*", Pattern.DOTALL);
    private final Pattern body = Pattern.compile(".*<body>(.*)</body>.*", Pattern.DOTALL);

    public Map<String, Object> process(String content) {
        Map<String, Object> chunks = new HashMap<String, Object>();

        chunks.put("head", extractHead(content));
        chunks.put("title", extractTitle(content));
        chunks.put("body", extractBody(content));
        return chunks;
    }

    private String extractHead(String html) {
        return extract(html, head);
    }

    private String extractTitle(String html) {
        return extract(html, title);
    }

    private String extractBody(String html) {
        return extract(html, body);
    }

    private String extract(String html, Pattern pattern) {
        Matcher matcher = pattern.matcher(html);
        if (!matcher.matches()) return null;
        return matcher.group(1);
    }
}
