package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

// https://stackoverflow.com/questions/47609005/adding-meta-data-with-itext-7

@DisplayName("verify PDF metadata feature")
class MetadataTests extends AbstractTest {

	private static Lorem lorem = LoremIpsum.getInstance();

	@Test
	void addTitle() throws Exception {
		String title = lorem.getWords(3);
		String targetPdf = RESULT_PATH + "/example-matadata.pdf";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addMetadata(title);
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).startsWith(title);
		}
	}

}
