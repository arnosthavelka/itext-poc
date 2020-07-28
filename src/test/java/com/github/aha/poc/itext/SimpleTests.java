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

// https://github.com/itext/itext7/blob/develop/kernel/src/test/java/com/itextpdf/kernel/pdf/PdfStringTest.java

@DisplayName("simple PDF generation")
class SimpleTests extends AbstractTest {

	public static final String RESULT = RESULT_PATH + "/hello.pdf";

	private static Lorem lorem = LoremIpsum.getInstance();

	@Test
	void testBuilder() throws Exception {
		String title = lorem.getWords(5, 10);
		String content = lorem.getParagraphs(2, 4);

		buildPDF(title, content);

		PdfReader reader = new PdfReader(RESULT);
		try (PdfDocument pdfDocument = new PdfDocument(reader)) {
//			PdfArray idArray = pdfDocument.getTrailer().getAsArray(PdfName.ID);
			String text = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(1), new LocationTextExtractionStrategy().setUseActualText(true));
			assertThat(text).startsWith(title);
		}
//		TestPdfListener listener = new TestPdfListener();
//		PdfContentStreamProcessor parser = new PdfContentStreamProcessor(listener);
//		parser.processContent(reader.getPageContent(pageNumber), reader.getPageResources(pageNumber));
//		assertEquals(title, listener.getStringBlocks().get(0));
	}

	private void buildPDF(String title, String content) {
		DocumentBuilder documentBuilder = new DocumentBuilder(RESULT);
		documentBuilder.init();
		documentBuilder.addTitle(title);
		documentBuilder.addParagraph(content);
		documentBuilder.generateDocument();
	}

}
