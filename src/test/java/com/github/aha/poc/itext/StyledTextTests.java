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
import static com.itextpdf.kernel.colors.ColorConstants.WHITE;
import static com.itextpdf.kernel.colors.ColorConstants.YELLOW;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.DoubleBorder;
import com.itextpdf.layout.borders.RidgeBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;

@DisplayName("Define text with differet styles")
class StyledTextTests extends AbstractTest {

	@Test
	void styledText() throws Exception {
		String targetPdf = RESULT_PATH + "/example-style-text.pdf";
		var title = lorem.getWords(3);

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		addColors(documentBuilder);
		addBackgroundColors(documentBuilder);
		addFonts(documentBuilder);
		addStyles(documentBuilder);
		addSize(documentBuilder);
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).isNotEmpty();
		}
	}

	@Test
	void styledParagraph() throws Exception {
		String targetPdf = RESULT_PATH + "/example-style-paragraph.pdf";
		var title = lorem.getWords(3);

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		addRotation(documentBuilder);
		addBorder(documentBuilder);
		addMargin(documentBuilder);
		addPadding(documentBuilder);
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).isNotEmpty();
		}
	}
	private void addColors(DocumentBuilder documentBuilder) {
		Paragraph paragraph = documentBuilder.createParagraph("Colors: ");
		addColouredText(documentBuilder, paragraph, "blue", BLUE);
		addColouredText(documentBuilder, paragraph, "cyan", CYAN);
		addColouredText(documentBuilder, paragraph, "dark gray", DARK_GRAY);
		addColouredText(documentBuilder, paragraph, "gray", GRAY);
		addColouredText(documentBuilder, paragraph, "green", GREEN);
		addColouredText(documentBuilder, paragraph, "light gray", LIGHT_GRAY);
		addColouredText(documentBuilder, paragraph, "magenta", MAGENTA);
		addColouredText(documentBuilder, paragraph, "orange", ORANGE);
		addColouredText(documentBuilder, paragraph, "pink", PINK);
		addColouredText(documentBuilder, paragraph, "red", RED);
		addColouredText(documentBuilder, paragraph, "yellow", YELLOW);
		documentBuilder.addParagraph(paragraph);
	}

	private void addColouredText(DocumentBuilder documentBuilder, Paragraph paragraph, String label, Color color) {
		paragraph.add(documentBuilder.createStyledText(label, TextStyle.builder().color(color).build())).add(", ");
	}

	private void addBackgroundColors(DocumentBuilder documentBuilder) {
		Paragraph paragraph = documentBuilder.createParagraph("Background colors: ");
		addBackgroundColouredText(documentBuilder, paragraph, "Merkur", WHITE, BLUE);
		addBackgroundColouredText(documentBuilder, paragraph, "Venus", DARK_GRAY, ORANGE);
		addBackgroundColouredText(documentBuilder, paragraph, "Earth", MAGENTA, YELLOW);
		documentBuilder.addParagraph(paragraph);
	}

	private void addBackgroundColouredText(DocumentBuilder documentBuilder, Paragraph paragraph, String label, Color color, Color backgroundColor) {
		paragraph.add(documentBuilder.createStyledText(label, TextStyle.builder().color(color).backgroundColor(backgroundColor).build())).add(", ");
	}

	private void addFonts(DocumentBuilder documentBuilder) {
		Paragraph paragraph = documentBuilder.createParagraph("Standard fonts: ");
		addTextWithFont(documentBuilder, paragraph, COURIER);
		addTextWithFont(documentBuilder, paragraph, HELVETICA);
		addTextWithFont(documentBuilder, paragraph, SYMBOL);
		addTextWithFont(documentBuilder, paragraph, TIMES_ROMAN);
		addTextWithFont(documentBuilder, paragraph, ZAPFDINGBATS);
		documentBuilder.addParagraph(paragraph);
	}

	private void addTextWithFont(DocumentBuilder documentBuilder, Paragraph paragraph, String fontFamily) {
		paragraph.add(documentBuilder.createStyledText(fontFamily, TextStyle.builder().fontFamily(fontFamily).build())).add(", ");
	}

	private void addStyles(DocumentBuilder documentBuilder) {
		Paragraph paragraph = documentBuilder.createParagraph("Styles: ");
		paragraph.add(documentBuilder.createStyledText("Bold", TextStyle.builder().bold(true).build())).add(", ");
		paragraph.add(documentBuilder.createStyledText("Italic", TextStyle.builder().italic(true).build())).add(", ");
		paragraph.add(documentBuilder.createStyledText("Underline", TextStyle.builder().underline(true).build())).add(", ");
		paragraph.add(documentBuilder.createStyledText("Line through", TextStyle.builder().lineThrough(true).build())).add(", ");
		documentBuilder.addParagraph(paragraph);
	}

	private void addSize(DocumentBuilder documentBuilder) {
		Paragraph paragraph = documentBuilder.createParagraph("Sizes: ");
		paragraph.add(documentBuilder.createStyledText("10", TextStyle.builder().fontSize(10f).build())).add(", ");
		paragraph.add(documentBuilder.createStyledText("30", TextStyle.builder().fontSize(30f).build())).add(", ");
		paragraph.add(documentBuilder.createStyledText("50", TextStyle.builder().fontSize(50f).build())).add(", ");
		documentBuilder.addParagraph(paragraph);
	}

	private void addRotation(DocumentBuilder documentBuilder) {
		documentBuilder.addParagraph(documentBuilder.createParagraph("Text rotations: "));
		for (float i = 45; i < 180; i = i + 45) {
			addRotatedParagrapgh(documentBuilder, i);
		}
	}

	private void addRotatedParagrapgh(DocumentBuilder documentBuilder, Float rotation) {
		var paragraphStyle = ParagraphStyle.builder().rotation(rotation).build();
		documentBuilder.addParagraph(documentBuilder.createStyledParagraph(rotation + " degree", paragraphStyle));
	}

	private void addBorder(DocumentBuilder documentBuilder) {
		documentBuilder.addParagraph(documentBuilder.createParagraph("Borders: "));
		addBorderedParagrapgh(documentBuilder, "Solid border", new SolidBorder(2f));
		addBorderedParagrapgh(documentBuilder, "3D border (Ridge)", new RidgeBorder(2f));
		addBorderedParagrapgh(documentBuilder, "Red dashed border", new DashedBorder(RED, 2f));
		addBorderedParagrapgh(documentBuilder, "Blue dotted border with 50% opacity", new DottedBorder(BLUE, 2f, 0.5f));
		addBorderedParagrapgh(documentBuilder, "Solid double border", new DoubleBorder(2f));
	}

	private void addBorderedParagrapgh(DocumentBuilder documentBuilder, String label, Border border) {
		documentBuilder.addParagraph(documentBuilder.createStyledParagraph(label, ParagraphStyle.builder().border(border).build()));
	}

	private void addMargin(DocumentBuilder documentBuilder) {
		documentBuilder.addParagraph(documentBuilder.createParagraph("Margins: "));
		for (float offset = 0; offset < 10; offset = offset + 3) {
			var paragraphStyle = ParagraphStyle.builder().margin(offset).border(new SolidBorder(1f)).build();
			documentBuilder.addParagraph(documentBuilder.createStyledParagraph(offset + " px", paragraphStyle));
		}
	}

	private void addPadding(DocumentBuilder documentBuilder) {
		documentBuilder.addParagraph(documentBuilder.createParagraph("Paddings: "));
		for (float offset = 0; offset < 10; offset = offset + 3) {
			var paragraphStyle = ParagraphStyle.builder().padding(offset).border(new SolidBorder(1f)).build();
			documentBuilder.addParagraph(documentBuilder.createStyledParagraph(offset + " px", paragraphStyle));
		}
	}

}



