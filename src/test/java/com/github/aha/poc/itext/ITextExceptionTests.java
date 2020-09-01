package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ITextExceptionTests {

	@Test
	@DisplayName("Creation of ITextException")
	void checkMessage() throws Exception {
		var errorMessage = "some message";

		var exception = new ITextException(errorMessage);

		assertThat(exception.getMessage()).isEqualTo(errorMessage);
	}

}
