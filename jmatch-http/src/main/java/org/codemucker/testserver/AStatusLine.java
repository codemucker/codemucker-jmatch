package org.codemucker.testserver;

import org.apache.http.StatusLine;
import org.codemucker.jpattern.generate.matcher.GenerateMatcher;
import org.apache.http.ProtocolVersion;
import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.PropertyMatcher;
import org.codemucker.jmutate.generate.CodeGenMeta;
import org.codemucker.jpattern.generate.IsGenerated;

@GenerateMatcher(generateFor=StatusLine.class)
public class AStatusLine extends org.codemucker.jmatch.PropertyMatcher<org.apache.http.StatusLine> {

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AStatusLine() {
		super(StatusLine.class);
	}

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public static AStatusLine with() {
		return new AStatusLine();
	}

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AStatusLine protocolVersion(
			final Matcher<? super ProtocolVersion> matcher) {
		matchProperty("protocolVersion", ProtocolVersion.class, matcher);
		return this;
	}

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AStatusLine reasonPhrase(final String val) {
		reasonPhrase(org.codemucker.jmatch.AString.equalTo(val));
		return this;
	}

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AStatusLine reasonPhrase(final Matcher<String> matcher) {
		matchProperty("reasonPhrase", String.class, matcher);
		return this;
	}

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AStatusLine statusCode(final int val) {
		statusCode(org.codemucker.jmatch.AnInt.equalTo(val));
		return this;
	}

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AStatusLine statusCode(final Matcher<Integer> matcher) {
		matchProperty("statusCode", Integer.class, matcher);
		return this;
	}

}
