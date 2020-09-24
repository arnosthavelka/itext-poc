package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ITextExceptionTests {

	@Test
	@DisplayName("Creation ITextException with just message")
	void justMessage() throws Exception {
		var errorMessage = "some message";

		var exception = new ITextException(errorMessage);

		assertThat(exception.getMessage()).isEqualTo(errorMessage);
	}

	@Test
	@DisplayName("Creation ITextException with message and the cause")
	void messageAndCause() throws Exception {
		var errorMessage = "some message";
		var cause = new RuntimeException("original message");

		var exception = new ITextException(errorMessage, cause);

		assertThat(exception.getMessage()).isEqualTo(errorMessage);
		assertThat(exception.getCause()).isSameAs(cause);
	}

}
