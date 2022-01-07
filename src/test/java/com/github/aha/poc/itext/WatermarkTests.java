package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor.getTextFromPage;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

class WatermarkTests extends AbstractPdfTest {

	private static final String SOURCE_PDF = "simple.pdf";

	@Test
	void generateWatermarkInPdf() throws IOException {
		String targetPdf = RESULT_PATH + "/example-watermark.pdf";
		var title = lorem.getWords(3);

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addParagraph(lorem.getParagraphs(2, 10));
		documentBuilder.addParagraph(lorem.getParagraphs(6, 10));
		documentBuilder.addParagraph(lorem.getParagraphs(3, 10));
		documentBuilder.addWatermark("PREVIEW");
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
				assertThat(getTextFromPage(pdfDocument.getPage(i), new LocationTextExtractionStrategy())).contains("PREVIEW");
			}
		}
	}

	@Test
	void modifyPdfWithWatermak() throws IOException {
		try (var sourcePdfDocument = new PdfDocument(new PdfReader(SOURCE_PDF))) {
			// TODO
		}
	}

}
