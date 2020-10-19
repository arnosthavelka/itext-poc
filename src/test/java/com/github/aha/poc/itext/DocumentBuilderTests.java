package com.github.aha.poc.itext;

import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.WriterProperties;

class DocumentBuilderTests {

	private static final String SOME_FILE_PDF = "target/some_file.pdf";

	@Test
	@DisplayName("should create instance of DocumentBuilder")
	void creation() throws FileNotFoundException {
		assertDoesNotThrow(() -> new DocumentBuilder(SOME_FILE_PDF));
	}

	@Test
	@DisplayName("should create instance of DocumentBuilder")
	void creationWithNull() throws FileNotFoundException {
		assertThrows(NullPointerException.class, () -> new DocumentBuilder(null));
	}

	@Test
	@DisplayName("should handle FileNotFoundException ")
	void createDocumentWithFailure() throws FileNotFoundException {
		var writeProperties = mock(WriterProperties.class);
		var docBuilder = spy(new DocumentBuilder(SOME_FILE_PDF));
		given(docBuilder.createPdfWriter(SOME_FILE_PDF, writeProperties)).willThrow(FileNotFoundException.class);

		assertThrows(ITextException.class, () -> docBuilder.createDocument(SOME_FILE_PDF, writeProperties));
	}

	@Test
	@DisplayName("should handle IOException ")
	void handleParagraphFailure() throws IOException {
		var docBuilder = spy(new DocumentBuilder(SOME_FILE_PDF));
		given(docBuilder.createPdfFont(HELVETICA)).willThrow(IOException.class);

		assertThrows(ITextException.class, () -> docBuilder.createFont(HELVETICA));
	}

}
