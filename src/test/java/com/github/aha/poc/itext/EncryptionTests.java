package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.EncryptionConstants.ALLOW_PRINTING;
import static com.itextpdf.kernel.pdf.EncryptionConstants.ENCRYPTION_AES_256;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.ReaderProperties;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import lombok.extern.slf4j.Slf4j;

@DisplayName("iText security features")
@Slf4j
class EncryptionTests extends AbstractTest {

	public static final String RESULT = RESULT_PATH + "/encryption.pdf";

	@Test
	@DisplayName("add password and restrict (remove) copy permission in newly generated PDF")
	void createPdfWithSecurity() throws Exception {
		var title = "PDF title";
		var content = "Some secured content";

		generateSecuredPDF("aha", title, content);

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(GENERATED_SECURED_PDF, buildReaderPropertiesWithPassword("aha")))) {
			String pdfContent = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
			log.debug("PDF content:\n{}", pdfContent);
			assertThat(pdfContent).startsWith(title);
		}

	}

	@Test
	@DisplayName("modify and protect existing PDF with password")
	void modifyAndProtectExistingPdfWithPassword() throws Exception {
		var additionalContent = "Additional secured content";

		try (Document document = new Document(createDocument(RESULT, "aha"))) {
			Paragraph paragraph = new Paragraph(additionalContent);
			paragraph.setFixedPosition(20, 0, 200);
			document.add(paragraph);
		}

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(RESULT, buildReaderPropertiesWithPassword("aha")))) {
			String pdfContent = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
			log.debug("PDF content:\n{}", pdfContent);
			assertThat(pdfContent).contains(additionalContent);
		}
	}

	private PdfDocument createDocument(String targetFilename, String userPassowrd) throws IOException {
		PdfReader reader = new PdfReader("hello-source.pdf");
		WriterProperties wp = new WriterProperties();
		wp.setStandardEncryption(userPassowrd.getBytes(), null, ALLOW_PRINTING /* | EncryptionConstants.ALLOW_COPY */, ENCRYPTION_AES_256);
		PdfWriter writer = new PdfWriter(RESULT, wp);
		return new PdfDocument(reader, writer);
	}

	ReaderProperties buildReaderPropertiesWithPassword(String userPassowrd) {
		ReaderProperties readerProperties = new ReaderProperties();
		readerProperties.setPassword(userPassowrd.getBytes());
		return readerProperties;
	}

}
