package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfReader;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

// https://stackoverflow.com/questions/47609005/adding-meta-data-with-itext-7

@DisplayName("verify PDF metadata feature")
class MetadataTests extends AbstractTest {

	private static Lorem lorem = LoremIpsum.getInstance();

	@Test
	void addTitle() throws Exception {
		var title = lorem.getWords(3);
		String targetPdf = RESULT_PATH + "/example-matadata.pdf";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addMetadata(null, null, null, null);
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			PdfDocumentInfo documentInfo = pdfDocument.getDocumentInfo();
			assertThat(documentInfo.getTitle()).isEqualTo(title);
			assertThat(documentInfo.getSubject()).isNull();
			assertThat(documentInfo.getAuthor()).isNull();
			assertThat(documentInfo.getCreator()).isNull();
		}
	}

	@Test
	void addMetadata() throws Exception {
		var title = lorem.getWords(3);
		var subject = lorem.getWords(5);
		var author = "Arny";
		var creator = "itext-poc";
		String targetPdf = RESULT_PATH + "/example-matadata.pdf";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addMetadata(null, subject, author, creator);
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			PdfDocumentInfo documentInfo = pdfDocument.getDocumentInfo();
			assertThat(documentInfo.getTitle()).isEqualTo(title);
			assertThat(documentInfo.getSubject()).isEqualTo(subject);
			assertThat(documentInfo.getAuthor()).isEqualTo(author);
			assertThat(documentInfo.getCreator()).isEqualTo(creator);
		}
	}

}
