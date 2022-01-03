package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.canvas.parser.EventType.RENDER_TEXT;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

// https://github.com/itext/itext7/blob/develop/kernel/src/test/java/com/itextpdf/kernel/pdf/PdfStringTest.java
// https://sodocumentation.net/itext/topic/5790/page-events--itext-5--versus-event-handlers-and-renderers--itext-7-
// https://stackoverflow.com/questions/40951776/manipulate-paths-color-etc-in-itext
// comparing PDF & Bruno's response: https://stackoverflow.com/questions/13703190/itext-api-for-pdf-comparison

@Slf4j
class SimpleTextTests extends AbstractPdfTest {

	@Test
	void loadPageContentAsWholeText() throws Exception {
		var title = lorem.getWords(3);
		var content = lorem.getParagraphs(2, 4);

		generatePDF(title, content);

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(GENERATED_PDF))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).startsWith(title);
		}
	}

	@Test
	void loadPageContentAsLines() throws Exception {
		var title = lorem.getWords(3);
		var content = lorem.getParagraphs(2, 4);

		generatePDF(title, content);

		try (
				PdfReader reader = new PdfReader(GENERATED_PDF);
				PdfDocument pdfDocument = new PdfDocument(reader)) {

			PdfDocumentContentParser pdfParser = new PdfDocumentContentParser(pdfDocument);
			TextExtractor textStrategy = new TextExtractor();
			pdfParser.processContent(1, textStrategy);
			var texts = textStrategy.getTexts();

			log.debug("{} found lines", texts);
			assertThat(title).isEqualTo(texts.get(0).getPdfString().getValue());
			assertThat(content).endsWith(texts.get(texts.size() - 1).getPdfString().getValue());
		}
	}

	static class TextExtractor implements IEventListener {

		@Getter
		private final List<TextRenderInfo> texts = new ArrayList<>();

		@Override
		public void eventOccurred(IEventData data, EventType type) {
			if (!RENDER_TEXT.equals(type)) {
				return;
			}
			texts.add((TextRenderInfo) data);

		}

		@Override
		public Set<EventType> getSupportedEvents() {
			return Set.of(RENDER_TEXT);
		}

	}

}
