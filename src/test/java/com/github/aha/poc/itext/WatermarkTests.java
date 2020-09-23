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

@DisplayName("verify PDF watermark feature")
class WatermarkTests extends AbstractTest {

	private static Lorem lorem = LoremIpsum.getInstance();

	@Test
	void addTitle() throws Exception {
		String targetPdf = RESULT_PATH + "/example-watermark.pdf";
		var title = lorem.getWords(3);

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addParagraph(lorem.getParagraphs(2, 4));
		documentBuilder.addParagraph(lorem.getParagraphs(6, 4));
		documentBuilder.addParagraph(lorem.getParagraphs(3, 4));
		documentBuilder.addWatermark("PREVIEW");
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
				assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getPage(i), new LocationTextExtractionStrategy())).contains("PREVIEW");
			}
		}
	}

}