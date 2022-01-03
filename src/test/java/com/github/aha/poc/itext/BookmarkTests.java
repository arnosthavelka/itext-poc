package com.github.aha.poc.itext;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNameTree;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class BookmarkTests extends AbstractPdfTest {

	@Test
	void addTitle() throws Exception {
		String targetPdf = RESULT_PATH + "/example-bookmark.pdf";
		var title = lorem.getWords(3);
		var parts = of("Part I.", "Part III.", "Part III.");

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		addDocumentPart(documentBuilder, parts.get(0), 20);
		addDocumentPart(documentBuilder, parts.get(1), 10);
		addDocumentPart(documentBuilder, parts.get(2), 20);
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			PdfNameTree destsTree = pdfDocument.getCatalog().getNameTree(PdfName.Dests);
			PdfOutline outlines = pdfDocument.getOutlines(false);
			outlines.getAllChildren().forEach(b -> {
				int pageNumber = pdfDocument.getPageNumber((PdfDictionary) b.getDestination().getDestinationPage(destsTree.getNames()));
				log.debug("Bookmark '{}' was found on page {}", b.getTitle(), pageNumber);
				assertThat(parts).contains(b.getTitle());
				assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getPage(pageNumber), new LocationTextExtractionStrategy()))
						.contains("--- " + b.getTitle() + "---");
			});
		}
	}

	private void addDocumentPart(DocumentBuilder documentBuilder, String partName, int minParagraphCount) {
		documentBuilder.addParagraph("--- " + partName + "---");
		documentBuilder.addBookmark(partName);
		documentBuilder.addParagraph(lorem.getParagraphs(minParagraphCount, 50));
	}

}
