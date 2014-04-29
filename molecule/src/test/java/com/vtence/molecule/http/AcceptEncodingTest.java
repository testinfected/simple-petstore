package com.vtence.molecule.http;

import com.vtence.molecule.support.MockRequest;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class AcceptEncodingTest {

    @Test public void
    selectsNoEncodingWhenThereAreNoCandidates() {
        assertSelected("gzip", in(), nullValue());
    }

    @Test public void
    selectsAcceptableEncodingWithHighestQuality() {
        assertSelected("compress; q=0.5, gzip", in("compress", "gzip"), equalTo("gzip"));
        assertSelected("gzip, deflate", in("deflate", "gzip"), equalTo("gzip"));
    }

    @Test public void
    selectsNoEncodingWhenCandidatesAreNotAcceptable() {
        assertSelected("compress, deflate; q=0", in("gzip", "deflate"), nullValue());
    }

    @Test public void
    considersIdentityEncodingAcceptableByDefault() {
        assertSelected("", in("gzip", "identity"), equalTo("identity"));
        assertSelected("deflate, gzip", in("identity"), equalTo("identity"));
        assertSelected("deflate; q=0, gzip; q=0", in("gzip", "deflate", "identity"),
                equalTo("identity"));
        assertSelected("*; q=0, identity; q=0.1", in("gzip", "deflate", "identity"),
                equalTo("identity"));
    }

    @Test public void
    considersIdentityEncodingNoLongerAcceptableWhenExplicitlyOrImplicitlyRefused() {
        assertSelected("identity; q=0", in("identity"), nullValue());
        assertSelected("*; q=0", in("identity"), nullValue());
    }

    @Test public void
    selectsFirstOfHighestQualityEncodingsWhenAnyIsAcceptable() {
        assertSelected("*", in("gzip", "deflate", "identity"), equalTo("gzip"));
        assertSelected("gzip; q=0.9, *", in("gzip", "deflate", "compress"), equalTo("deflate"));
    }

    @Test public void
    handlesAbsenceOfAcceptEncodingHeader() {
        AcceptEncoding accept = AcceptEncoding.of(new MockRequest());
        assertThat("selected encoding", accept.selectBestEncoding("gzip", "identity"),
                equalTo("identity"));
    }

    private void assertSelected(String header,
                                List<String> candidates,
                                Matcher<? super String> matcher) {
        AcceptEncoding acceptEncoding = new AcceptEncoding(header);
        assertThat("selected encoding", acceptEncoding.selectBestEncoding(candidates), matcher);
    }

    public static List<String> in(String... candidates) {
        return Arrays.asList(candidates);
    }
}
