package org.codemucker.jmatch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.codemucker.jmatch.AbstractMatcher.AllowNulls;

public final class AnInstance {

    public static final <T> List<Matcher<T>> equalToAll(final T... expectAll) {
        List<Matcher<T>> matchers = new ArrayList<Matcher<T>>(expectAll.length);
        for (int i = 0; i < expectAll.length; i++) {
            matchers.add(equalTo(expectAll[i]));
        }
        return matchers;
    }

    @SuppressWarnings("unchecked")
    public static final <T> Matcher<T> notNull() {
        return (Matcher<T>) Logical.not(equalToNull());
    }

    public static final <T> Matcher<T> equalToNull() {
        return equalTo(null);
    }

    public static final <T> Matcher<T> equalTo(final T expect) {
        return new AbstractMatcher<T>(AllowNulls.YES) {
            @Override
            public boolean matchesSafely(T found, MatchDiagnostics diag) {
                if (expect == null && found == null) {
                    return true;
                }
                if (expect == null || found == null) {
                    return false;
                }
                return expect.equals(found);
            }

            @Override
            public void describeTo(Description desc) {
                super.describeTo(desc);
                desc.value("an instance equal to ", expect);
            }
        };
    }

    public static <T> Matcher<T> reflectionEqualsExceptFields(T expect, String[] excudeFields) {
        return new EqualsMatcher<T>(expect, excudeFields);
    }

    public static <T> Matcher<T> reflectionEquals(T expect) {
        return new EqualsMatcher<T>(expect);
    }

    private static class EqualsMatcher<T> extends AbstractNotNullMatcher<T> {

        private final T expect;
        private final String[] exclude;

        public EqualsMatcher(T expect) {
            this(expect, null);
        }

        public EqualsMatcher(T expect, String[] exclude) {
            super();
            org.apache.commons.lang3.Validate.notNull(expect);
            this.expect = expect;
            this.exclude = exclude;
        }

        @Override
        public void describeTo(Description desc) {
            String s = ToStringBuilder.reflectionToString(expect, ToStringStyle.SHORT_PREFIX_STYLE);

            desc.text("expect %s excluding fields %s", s, exclude);
        }

        @Override
        public boolean matchesSafely(T item, MatchDiagnostics diag) {
            return EqualsBuilder.reflectionEquals(expect, item, exclude);
        }

    }
}
