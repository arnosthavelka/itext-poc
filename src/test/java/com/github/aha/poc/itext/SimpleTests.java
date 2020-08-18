package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import lombok.extern.slf4j.Slf4j;

// https://github.com/itext/itext7
// https://github.com/itext/itext7/blob/develop/kernel/src/test/java/com/itextpdf/kernel/pdf/PdfStringTest.java
// https://sodocumentation.net/itext/topic/5790/page-events--itext-5--versus-event-handlers-and-renderers--itext-7-
// https://stackoverflow.com/questions/40951776/manipulate-paths-color-etc-in-itext

@DisplayName("simple PDF generation")
@Slf4j
class SimpleTests extends AbstractTest {

	public static final String GENERATED_PDF = RESULT_PATH + "/hello.pdf";

	private static Lorem lorem = LoremIpsum.getInstance();

	@Test
	void testBuilder() throws Exception {
		String title = lorem.getWords(3);
		String content = lorem.getParagraphs(2, 4);

		generatePDF(title, content);

		PdfReader reader = new PdfReader(GENERATED_PDF);

		try (PdfDocument pdfDocument = new PdfDocument(reader)) {
			for (int i = 1; i <= pdfDocument.getNumberOfPdfObjects(); i++) {
				PdfObject pdfObject = pdfDocument.getPdfObject(i);
				if (pdfObject == null) { // || !pdfObject.isStream()
					continue;
				}
				log.info("object: index={}, type={}", i, pdfObject.getType());
//				byte[] bytes = ((PdfStream) pdfObject).getBytes(false); // new String(bytes, "UTF-8")
				log.info("value={}, ref={}", pdfObject.toString(), pdfObject.getIndirectReference().getRefersTo().toString());
			}
//			PdfArray idArray = pdfDocument.getTrailer().getAsArray(PdfName.ID);
//			String text = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(1));
			log.info("\n\ntest value={}", pdfDocument.getFirstPage().getPdfObject());
			String text = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
			assertThat(text).startsWith(title);
		}
//		TestPdfListener listener = new TestPdfListener();
//		PdfContentStreamProcessor parser = new PdfContentStreamProcessor(listener);
//		parser.processContent(reader.getPageContent(pageNumber), reader.getPageResources(pageNumber));
//		assertEquals(title, listener.getStringBlocks().get(0));
	}

	@Test
	void testBuilder2() throws Exception {
		String title = lorem.getWords(3);
		String content = lorem.getParagraphs(2, 4);

		generatePDF(title, content);

		try (
				PdfReader reader = new PdfReader(GENERATED_PDF);
				PdfDocument pdfDocument = new PdfDocument(reader)) {

			PdfDocumentContentParser pdfParser = new PdfDocumentContentParser(pdfDocument);
			LocationTextExtractionStrategy textStrategy = new LocationTextExtractionStrategy();
			pdfParser.processContent(1, textStrategy);
			var text = textStrategy.getResultantText();

			assertThat(text).startsWith(title);
		}
	}

	private void generatePDF(String title, String content) {
		DocumentBuilder documentBuilder = new DocumentBuilder(GENERATED_PDF);
		documentBuilder.init();
		documentBuilder.addTitle(title);
		documentBuilder.addParagraph(content);
		documentBuilder.generateDocument();
	}

}
