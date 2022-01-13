package com.github.aha.poc.itext;

import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA;
import static com.itextpdf.kernel.colors.ColorConstants.BLUE;
import static com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor.getTextFromPage;
import static com.itextpdf.layout.properties.TextAlignment.CENTER;
import static com.itextpdf.layout.properties.VerticalAlignment.TOP;
import static java.lang.Math.PI;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

class WatermarkTests extends AbstractPdfTest {

	static final String SOURCE_PDF = "simple.pdf";
	static final float ROTATION_20_DEGREE = (float) (PI / 180 * 20);

	@Test
	void generateWatermarkToPdf() throws IOException {
		String targetPdf = RESULT_PATH + "/example-watermark-generated.pdf";
		var title = lorem.getWords(3);
		var watermark = "PREVIEW";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addParagraph(lorem.getParagraphs(2, 10));
		documentBuilder.addParagraph(lorem.getParagraphs(6, 10));
		documentBuilder.addParagraph(lorem.getParagraphs(3, 10));
		documentBuilder.addWatermark(watermark);
		documentBuilder.generateDocument();

		verifyPreviewWatermark(targetPdf, watermark);
	}

	@Test
	void addWatermarkToExistingPdf() throws IOException {
		String targetPdf = RESULT_PATH + "/example-watermark-modified.pdf";
		var watermark = "CONFIDENTIAL";

		try (var pdfDoc = new PdfDocument(new PdfReader(SOURCE_PDF), new PdfWriter(targetPdf))) {
			var document = new Document(pdfDoc);
			addWatermark(document, watermark);
		}

		verifyPreviewWatermark(targetPdf, watermark);
	}

	void verifyPreviewWatermark(String targetPdf, String watermark) throws IOException {
		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
				assertThat(getTextFromPage(pdfDocument.getPage(i), new LocationTextExtractionStrategy())).contains(watermark);
			}
		}
	}

	void addWatermark(Document document, String watermark) {
		float fontSize = 50;
		var paragraph = createWatermarkParagraph(watermark, fontSize);
		var transparentGraphicState = new PdfExtGState().setFillOpacity(0.5f);

		for (int i = 1; i <= document.getPdfDocument().getNumberOfPages(); i++) {
			addWatermarkToPage(document, i, paragraph, transparentGraphicState, fontSize);
		}
	}

	void addWatermarkToPage(Document document, int pageIndex, Paragraph paragraph, PdfExtGState graphicState, float fontSize) {
		var pdfDoc = document.getPdfDocument();
		var pdfPage = pdfDoc.getPage(pageIndex);
		Rectangle pageSize = pdfPage.getPageSizeWithRotation();

		float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
		float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
		var over = new PdfCanvas(pdfDoc.getPage(pageIndex));
		over.saveState();
		over.setExtGState(graphicState);
		float xOffset = fontSize / 2;
		float yOffset = 330f;
		document.showTextAligned(paragraph, x - xOffset, y + yOffset, pageIndex, CENTER, TOP, ROTATION_20_DEGREE);
		over.restoreState();
	}

	Paragraph createWatermarkParagraph(String watermark, float fontSize) {
		var text = new Text(watermark);
		text.setFont(createFont(HELVETICA));
		text.setFontSize(fontSize);
		text.setFontColor(BLUE);
		return new Paragraph(text);
	}

	PdfFont createFont(String fontFamily) {
		try {
			return PdfFontFactory.createFont(fontFamily);
		} catch (IOException e) {
			throw new PdfException("Font creation failed", e);
		}
	}

}
