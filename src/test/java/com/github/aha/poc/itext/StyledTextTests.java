package com.github.aha.poc.itext;

import static com.itextpdf.io.font.constants.StandardFonts.COURIER;
import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA;
import static com.itextpdf.io.font.constants.StandardFonts.SYMBOL;
import static com.itextpdf.io.font.constants.StandardFonts.TIMES_ROMAN;
import static com.itextpdf.io.font.constants.StandardFonts.ZAPFDINGBATS;
import static com.itextpdf.kernel.colors.ColorConstants.BLUE;
import static com.itextpdf.kernel.colors.ColorConstants.CYAN;
import static com.itextpdf.kernel.colors.ColorConstants.DARK_GRAY;
import static com.itextpdf.kernel.colors.ColorConstants.GRAY;
import static com.itextpdf.kernel.colors.ColorConstants.GREEN;
import static com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY;
import static com.itextpdf.kernel.colors.ColorConstants.MAGENTA;
import static com.itextpdf.kernel.colors.ColorConstants.ORANGE;
import static com.itextpdf.kernel.colors.ColorConstants.PINK;
import static com.itextpdf.kernel.colors.ColorConstants.RED;
import static com.itextpdf.kernel.colors.ColorConstants.YELLOW;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.element.Paragraph;

@DisplayName("Define text with differet styles")
class StyledTextTests extends AbstractTest {

	@Test
	void styledText() throws Exception {
		String targetPdf = RESULT_PATH + "/example-style.pdf";
		var title = lorem.getWords(3);

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		addColors(documentBuilder);
		addFonts(documentBuilder);
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).startsWith(title);
		}
	}

	private void addColors(DocumentBuilder documentBuilder) throws IOException {
		Paragraph colorParagraph = documentBuilder.createParagraph("Colors: ");
		addStyledText(documentBuilder, colorParagraph, "blue", BLUE);
		addStyledText(documentBuilder, colorParagraph, "cyan", CYAN);
		addStyledText(documentBuilder, colorParagraph, "dark gray", DARK_GRAY);
		addStyledText(documentBuilder, colorParagraph, "gray", GRAY);
		addStyledText(documentBuilder, colorParagraph, "green", GREEN);
		addStyledText(documentBuilder, colorParagraph, "light gray", LIGHT_GRAY);
		addStyledText(documentBuilder, colorParagraph, "magenta", MAGENTA);
		addStyledText(documentBuilder, colorParagraph, "orange", ORANGE);
		addStyledText(documentBuilder, colorParagraph, "pink", PINK);
		addStyledText(documentBuilder, colorParagraph, "red", RED);
		addStyledText(documentBuilder, colorParagraph, "yellow", YELLOW);
		documentBuilder.addParagraph(colorParagraph);
	}

	private void addFonts(DocumentBuilder documentBuilder) throws IOException {
		Paragraph colorParagraph = documentBuilder.createParagraph("Fonts: ");
		addStyledText(documentBuilder, colorParagraph, COURIER);
		addStyledText(documentBuilder, colorParagraph, HELVETICA);
		addStyledText(documentBuilder, colorParagraph, SYMBOL);
		addStyledText(documentBuilder, colorParagraph, TIMES_ROMAN);
		addStyledText(documentBuilder, colorParagraph, ZAPFDINGBATS);
		documentBuilder.addParagraph(colorParagraph);
	}

	private void addStyledText(DocumentBuilder documentBuilder, Paragraph paragraph, String label, Color color) throws IOException {
		paragraph.add(documentBuilder.createStyledText(label, color, null, false, false, false));
		paragraph.add(", ");
	}

	private void addStyledText(DocumentBuilder documentBuilder, Paragraph paragraph, String fontFamily) throws IOException {
		paragraph.add(documentBuilder.createStyledText(fontFamily, null, fontFamily, false, false, false));
		paragraph.add(", ");
	}

}
