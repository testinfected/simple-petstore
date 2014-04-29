package com.vtence.molecule.middlewares;

import com.vtence.molecule.Body;
import com.vtence.molecule.lib.AbstractMiddleware;
import com.vtence.molecule.lib.ChunkedBody;
import com.vtence.molecule.http.HttpStatus;
import com.vtence.molecule.Request;
import com.vtence.molecule.Response;
import com.vtence.molecule.http.AcceptEncoding;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

import static com.vtence.molecule.http.HeaderNames.CONTENT_ENCODING;
import static com.vtence.molecule.http.HeaderNames.CONTENT_LENGTH;
import static com.vtence.molecule.middlewares.Compressor.Codings.identity;

public class Compressor extends AbstractMiddleware {

    static enum Codings {

        gzip {
            public void encode(Response response) throws IOException {
                response.remove(CONTENT_LENGTH);
                response.set(CONTENT_ENCODING, name());
                response.body(new GZipStream(response.body()));
            }
        },

        deflate {
            public void encode(Response response) throws IOException {
                response.remove(CONTENT_LENGTH);
                response.set(CONTENT_ENCODING, name());
                response.body(new DeflateStream(response.body()));
            }
        },

        identity {
            public void encode(Response response) throws IOException {
            }
        };

        public abstract void encode(Response response) throws IOException;

        public static String[] all() {
            List<String> all = new ArrayList<String>();
            for (Codings coding : values()) {
                all.add(coding.name());
            }
            return all.toArray(new String[all.size()]);
        }

        private static class GZipStream extends ChunkedBody {
            private final Body body;

            public GZipStream(Body body) {
                this.body = body;
            }

            public void writeTo(OutputStream out, Charset charset) throws IOException {
                GZIPOutputStream zip = new GZIPOutputStream(out);
                try {
                    body.writeTo(zip, charset);
                } finally {
                    zip.finish();
                }
            }

            public void close() throws IOException {
                body.close();
            }
        }

        private static class DeflateStream extends ChunkedBody {
            private final Body body;

            public DeflateStream(Body body) {
                this.body = body;
            }

            public void writeTo(OutputStream out, Charset charset) throws IOException {
                Deflater zlib = new Deflater(Deflater.DEFAULT_COMPRESSION, true);
                DeflaterOutputStream deflate = new DeflaterOutputStream(out, zlib);
                try {
                    body.writeTo(deflate, charset);
                } finally {
                    deflate.finish();
                    zlib.end();
                }
            }

            public void close() throws IOException {
                body.close();
            }
        }
    }

    public void handle(Request request, final Response response) throws Exception {
        forward(request, response);

        if (response.empty() || alreadyEncoded(response)) {
            return;
        }

        String encoding = selectBestAvailableEncodingFor(request);
        if (encoding != null) {
            Codings coding = Codings.valueOf(encoding);
            coding.encode(response);
        } else {
            notAcceptable(response);
        }
    }

    private String selectBestAvailableEncodingFor(Request request) {
        AcceptEncoding acceptEncoding = AcceptEncoding.of(request);
        return acceptEncoding.selectBestEncoding(Codings.all());
    }

    private boolean alreadyEncoded(Response response) {
        String contentEncoding = response.get(CONTENT_ENCODING);
        return contentEncoding != null && !isIdentity(contentEncoding);
    }

    private boolean isIdentity(String contentEncoding) {
        return contentEncoding.matches(atWordBoundaries(identity.name()));
    }

    private String atWordBoundaries(String text) {
        return "\\b" + text + "\\b";
    }

    private void notAcceptable(Response response) throws IOException {
        response.status(HttpStatus.NOT_ACCEPTABLE);
        response.contentType("text/plain");
        response.body("An acceptable encoding could not be found");
    }
}
