package org.codemucker.testserver;

import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.codemucker.jmatch.Matcher;
import org.codemucker.jmutate.generate.CodeGenMeta;
import org.codemucker.jpattern.generate.IsGenerated;
import org.codemucker.jpattern.generate.matcher.GenerateMatcher;
import org.codemucker.jmatch.PropertyMatcher;

@GenerateMatcher(generateFor=HttpResponse.class)
public class AHttpResponse extends org.codemucker.jmatch.PropertyMatcher<org.apache.http.HttpResponse>{


	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AHttpResponse() {
		super(HttpResponse.class);
	}

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public static AHttpResponse with() {
		return new AHttpResponse();
	}

	public AHttpResponse statusCode(int status) {
		statusLine(AStatusLine.with().statusCode(status));
		return this;
	}

	public AHttpResponse statusReason(String reason) {
		statusLine(AStatusLine.with().reasonPhrase(reason));
		return this;
	}
	
	public AHttpResponse statusReason(Matcher<String> matcher) {
		statusLine(AStatusLine.with().reasonPhrase(matcher));
		return this;
	}
	
	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AHttpResponse entity(final Matcher<? super HttpEntity> matcher) {
		matchProperty("entity", HttpEntity.class, matcher);
		return this;
	}

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AHttpResponse locale(final Matcher<? super Locale> matcher) {
		matchProperty("locale", Locale.class, matcher);
		return this;
	}

	@IsGenerated(by = CodeGenMeta.matcherGenerator)
	public AHttpResponse statusLine(final Matcher<? super StatusLine> matcher) {
		matchProperty("statusLine", StatusLine.class, matcher);
		return this;
	}

}
