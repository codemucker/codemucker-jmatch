package org.codemucker.jmatch;

import java.io.IOException;
import java.io.InputStream;

import org.codemucker.jmatch.util.IOUtils;

public class AnInputStream extends ObjectMatcher<InputStream> {

    public static AnInputStream with() {
        return new AnInputStream();
    }

    public AnInputStream() {
        super(InputStream.class);
    }

    public AnInputStream stringContent(final String encoding, final Matcher<String> matcher){
        addMatcher(new AbstractNotNullMatcher<InputStream>() {

            @Override
            protected boolean matchesSafely(InputStream actual, MatchDiagnostics diag) {
                String s;
                try {
                    s = IOUtils.readToString(actual,encoding);
                } catch (IOException e) {
                    throw new JMatchException("error reading inputstream as string",e);
                }
                return diag.tryMatch(this, s, matcher);
            }
            
            @Override
            public void describeTo(Description desc) {
                desc.value("stream as string (" + encoding + ") is", matcher);
            }
        });
        return this;
    }
    public AnInputStream byteContent(final Matcher<byte[]> matcher){
        addMatcher(new AbstractNotNullMatcher<InputStream>() {

            @Override
            protected boolean matchesSafely(InputStream actual, MatchDiagnostics diag) {
                byte[] bytes;
                try {
                    bytes = IOUtils.readToBytes(actual);
                } catch (IOException e) {
                    throw new JMatchException("error reading inputstream as bytes",e);
                }
                return diag.tryMatch(this, bytes, matcher);
            }
            
            @Override
            public void describeTo(Description desc) {
                desc.value("stream as bytes is", matcher);
            }
        });
        return this;
    }
    
}
