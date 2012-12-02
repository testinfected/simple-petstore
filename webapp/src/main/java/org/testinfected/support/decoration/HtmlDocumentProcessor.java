package org.testinfected.support.decoration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlDocumentProcessor implements ContentProcessor {

    private static final int TEXT = 1;
    private static final int NAME = 1;
    private static final int CONTENT = 2;

    private static final Pattern HEAD = Pattern.compile("<head>\n?(.*?)\n?</head>", Pattern.DOTALL);
    private static final Pattern TITLE = Pattern.compile("<title>\n?(.*?)\n?</title>\\s*\n?", Pattern.DOTALL);
    private static final Pattern BODY = Pattern.compile("<body>\n?(.*?)\n?</body>", Pattern.DOTALL);
    private static final Pattern META = Pattern.compile("<meta name=\"([^\"]*)\" content=\"([^\"]*)\"", Pattern.DOTALL);

    public Map<String, Object> process(String html) {
        Map<String, Object> chunks = new HashMap<String, Object>();
        addHead(chunks, html);
        addTitle(chunks, html);
        addBody(chunks, html);
        addMetaData(chunks, html);
        return chunks;
    }

    private void addHead(Map<String, Object> chunks, String html) {
        String head = extract(html, HEAD);
        if (head == null) return;
        chunks.put("head", stripTitle(head));
    }

    private String stripTitle(String head) {
        return TITLE.matcher(head).replaceFirst("");
    }

    private String extract(String html, Pattern pattern) {
        Matcher matcher = pattern.matcher(html);
        if (!matcher.find()) return null;
        return matcher.group(TEXT);
    }

    private void addTitle(Map<String, Object> chunks, String html) {
        String head = extract(html, HEAD);
        if (head == null) return;
        String title = extract(head, TITLE);
        if (title == null) return;
        chunks.put("title", title.trim());
    }

    private void addBody(Map<String, Object> chunks, String html) {
        String body = extract(html, BODY);
        if (body == null) return;
        chunks.put("body", body);
    }

    private void addMetaData(Map<String, Object> chunks, String head) {
        Matcher matcher = META.matcher(head);
        while (matcher.find()) {
            chunks.put("meta[" + matcher.group(NAME) + "]", matcher.group(CONTENT));
        }
    }
}
