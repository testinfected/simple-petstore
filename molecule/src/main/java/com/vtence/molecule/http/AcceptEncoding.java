package com.vtence.molecule.http;

import com.vtence.molecule.Request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.vtence.molecule.http.HeaderNames.ACCEPT_ENCODING;

public class AcceptEncoding {
    private final Header header;

    public static AcceptEncoding of(Request request) {
        String header = request.header(ACCEPT_ENCODING);
        return header != null ? new AcceptEncoding(header) : new AcceptEncoding("");
    }

    public AcceptEncoding(String header) {
        this(new Header(header));
    }

    public AcceptEncoding(Header header) {
        this.header = header;
    }

    public String selectBestEncoding(String... candidates) {
        return selectBestEncoding(Arrays.asList(candidates));
    }

    public String selectBestEncoding(Collection<String> candidates) {
        List<Header.Value> contentCodings = explicitContentCodings(candidates);
        List<String> acceptableEncodings = listAcceptable(contentCodings);
        for (String acceptable : acceptableEncodings) {
            if (candidates.contains(acceptable)) return acceptable;
        }
        return null;
    }

    private List<Header.Value> explicitContentCodings(Collection<String> availableEncodings) {
        List<Header.Value> codings = new ArrayList<Header.Value>();

        for (Header.Value accept: header.all()) {
            if (accept.is("*")) {
                List<String> others = new ArrayList<String>(availableEncodings);
                others.removeAll(listValues(header.all()));
                for (String other : others) {
                    codings.add(new Header.Value(other, accept.parameters()));
                }
            } else {
                codings.add(accept);
            }
        }

        return codings;
    }

    private List<String> listAcceptable(List<Header.Value> encodings) {
        List<String> candidates = listValues(encodings);
        if (!candidates.contains("identity")) candidates.add("identity");

        for (Header.Value encoding : encodings) {
            if (!encoding.acceptable()) candidates.remove(encoding.value());
        }
        return candidates;
    }

    private List<String> listValues(List<Header.Value> entries) {
        List<String> values = new ArrayList<String>();
        for (Header.Value entry: entries) {
            values.add(entry.value());
        }
        return values;
    }
}
