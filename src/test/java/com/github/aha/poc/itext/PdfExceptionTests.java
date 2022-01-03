package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PdfExceptionTests {

	@Test
	void exceptionJustWithMessage() throws Exception {
		var errorMessage = "some message";

		var exception = new PdfException(errorMessage);

		assertThat(exception.getMessage()).isEqualTo(errorMessage);
	}

	@Test
	void exceptionWithMessageAndCause() throws Exception {
		var errorMessage = "some message";
		var cause = new RuntimeException("original message");

		var exception = new PdfException(errorMessage, cause);

		assertThat(exception.getMessage()).isEqualTo(errorMessage);
		assertThat(exception.getCause()).isSameAs(cause);
	}

}
