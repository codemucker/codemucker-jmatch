package org.codemucker.jmatch;

import java.util.Arrays;

import org.codemucker.jmatch.AbstractMatcher.AllowNulls;

public class AByteArray {

    public static final Matcher<byte[]> notNull() {
        return new AbstractMatcher<byte[]>(AllowNulls.NO) {
            @Override
            public boolean matchesSafely(byte[] found, MatchDiagnostics ctxt) {
                return true;
            }
            
            @Override
            public void describeTo(Description desc) {
                //super.describeTo(desc);
                desc.text("a byte[] not null");
            }
        };
    }

    public static final Matcher<byte[]> notEqualTo(final byte[] expect) {
        return Logical.not(equalTo(expect));
    }

    public static final Matcher<byte[]> equalTo(final byte[] expect) {
        return new AbstractMatcher<byte[]>(AllowNulls.YES) {
            @Override
            public boolean matchesSafely(byte[] found, MatchDiagnostics ctxt) {
                if (expect == null && found == null) {
                    return true;
                }
                if (expect == null) {
                    ctxt.mismatched("expected null byte[]");
                    return false;
                }
                if (found == null) {
                    ctxt.mismatched("expected non null byte[] of length %1", expect.length);
                    return false;
                }
                if (expect.length != found.length) {
                    ctxt.mismatched("expected length %1 but was %2", expect.length, found.length);
                    return false;
                }
                return expect.equals(found);
            }

            @Override
            public void describeTo(Description desc) {
                super.describeTo(desc);
                desc.text("a byte[] equal to " + Arrays.toString(expect));
            }
        };
    }
}
