package com.github.aha.poc.itext;

import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.WriterProperties;

class DocumentBuilderTests {

	private static final String SOME_FILE_PDF = "target/some_file.pdf";

	@Test
	void createEmptyPdf() {
		assertDoesNotThrow(() -> new DocumentBuilder(SOME_FILE_PDF));
	}

	@Test
	void failOnNull() {
		assertThrows(NullPointerException.class, () -> new DocumentBuilder(null));
	}

	@Test
	void failOnFileNotFoundException() throws FileNotFoundException {
		var writeProperties = mock(WriterProperties.class);
		var docBuilder = spy(new DocumentBuilder(SOME_FILE_PDF));
		given(docBuilder.createPdfWriter(SOME_FILE_PDF, writeProperties)).willThrow(FileNotFoundException.class);

		assertThrows(PdfException.class, () -> docBuilder.createDocument(SOME_FILE_PDF, writeProperties));
	}

	@Test
	void failOnIOException() throws IOException {
		var docBuilder = spy(new DocumentBuilder(SOME_FILE_PDF));
		given(docBuilder.createPdfFont(HELVETICA)).willThrow(IOException.class);

		assertThrows(PdfException.class, () -> docBuilder.createFont(HELVETICA));
	}

}
