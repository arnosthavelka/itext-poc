package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class DzoneTests {

	private static Lorem lorem = LoremIpsum.getInstance();

	@Test
	void generateSimpleContent() throws Exception {
		var content = lorem.getParagraphs(2, 4);

		String simplePdf = "target/dzone-simple.pdf";
		try {
			WriterProperties wp = new WriterProperties();
			wp.setPdfVersion(PdfVersion.PDF_2_0);
			PdfWriter writer = new PdfWriter(simplePdf, wp);
			PdfDocument pdfDocument = new PdfDocument(writer);
			Document document = new Document(pdfDocument);
			document.add(new Paragraph(content));
			document.close();
		} catch (FileNotFoundException e) {
			log.error("Creating PDF failed", e);
			throw new ITextException(e.getMessage());
		}

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(simplePdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy()))
					.startsWith(content.substring(0, 10));
		}
	}

}
