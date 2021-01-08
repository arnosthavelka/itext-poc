package com.github.aha.poc.itext;

import static com.itextpdf.io.font.constants.StandardFonts.COURIER;
import static com.itextpdf.kernel.colors.ColorConstants.BLUE;
import static com.itextpdf.kernel.colors.ColorConstants.GREEN;
import static com.itextpdf.kernel.colors.ColorConstants.RED;
import static com.itextpdf.kernel.colors.ColorConstants.YELLOW;
import static java.lang.Math.PI;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

class DzoneIntroTests {

	final static Logger log = LoggerFactory.getLogger(DzoneIntroTests.class);

	@Test
	void simpleText() throws Exception {
		var textContent = "Sapiensplendide noluisse aliquip altera natoque veniam principes.  "
				+ "Pulvinareruditi sanctus corrumpit hendrerit adhuc sapientem ridens luptatum nihil "
				+ "enim justo aliquid nascetur causae consectetur a quem dolorum vestibulum.  "
				+ "Utroquesuscipit duo tristique tellus saperet hendrerit maiestatis.  Suashendrerit "
				+ "accommodare vituperatoribus audire praesent facilisi inani efficiantur pro volutpat "
				+ "feugait inani eripuit dicit nihil nostrum etiam odio inceptos.  Pulvinarnisi magna "
				+ "noluisse saperet equidem elementum tantas intellegebat mollis verterem alia regione "
				+ "quidam.";

		String simplePdf = "target/dzone-simple-text.pdf";
		WriterProperties wp = new WriterProperties();
		wp.setPdfVersion(PdfVersion.PDF_2_0);
		try (PdfWriter writer = new PdfWriter(simplePdf, wp);
				PdfDocument pdfDocument = new PdfDocument(writer);
				Document document = new Document(pdfDocument)) {

			document.add(new Paragraph(textContent));

		} catch (FileNotFoundException e) {
			log.error("Creating PDF failed", e);
			throw new ITextException(e.getMessage());
		}

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(simplePdf))) {
			String firstPageContent = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
			assertThat(firstPageContent).startsWith(textContent.substring(0, 10));
		}
	}

	@Test
	void styledText() throws Exception {
		var label1 = "big blue Courier font";
		var label2 = "underlined bold default font with yellow background";
		var label3 = "crossed italic";

		String simplePdf = "target/dzone-styled-text.pdf";
		try (PdfWriter writer = new PdfWriter(simplePdf);
				PdfDocument pdfDocument = new PdfDocument(writer);
				Document document = new Document(pdfDocument)) {

			Paragraph paragraph = new Paragraph();
			paragraph.add(styledText(label1, BLUE, null, COURIER, 30f, false, false, false, false));
			paragraph.add("\n");
			paragraph.add(styledText(label2, RED, YELLOW, null, null, true, false, true, false));
			paragraph.add("\n");
			paragraph.add(styledText(label3, null, null, null, null, false, true, false, true));
			document.add(paragraph);

		} catch (FileNotFoundException e) {
			log.error("Creating PDF failed", e);
			throw new ITextException(e.getMessage());
		}

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(simplePdf))) {
			String firstPageContent = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
			assertThat(firstPageContent).startsWith(label1);
			assertThat(firstPageContent).contains(label2);
			assertThat(firstPageContent).endsWith(label3);
		}
	}

	public Text styledText(String label, Color color,	Color backgroundColor, String fontFamily, Float fontSize, boolean isBold, boolean isItalic, boolean isUnderline, boolean isLineThrough) {
		Text text = new Text(label);
		if (nonNull(color)) {
			text.setFontColor(color);
		}
		if (nonNull(backgroundColor)) {
			text.setBackgroundColor(backgroundColor);
		}
		if (nonNull(fontFamily)) {
			text.setFont(createFont(fontFamily));
		}
		if (nonNull(fontSize)) {
			text.setFontSize(fontSize);
		}
		if (isBold) {
			text.setBold();
		}
		if (isItalic) {
			text.setItalic();
		}
		if (isUnderline) {
			text.setUnderline();
		}
		if (isLineThrough) {
			text.setLineThrough();
		}
		return text;
	}

	PdfFont createFont(String fontFamily) {
		try {
			return PdfFontFactory.createFont(fontFamily);
		} catch (IOException e) {
			throw new ITextException("Font creation failed", e);
		}
	}

	@Test
	void styledParagraph() throws Exception {
		var label1 = "45° rotation";
		var label2 = "green dashed border";
		var label3 = "6px margin & 3px padding & solid border";

		String simplePdf = "target/dzone-styled-paragraph.pdf";
		try (PdfWriter writer = new PdfWriter(simplePdf);
				PdfDocument pdfDocument = new PdfDocument(writer);
				Document document = new Document(pdfDocument)) {

			Paragraph rotatedParagraph = new Paragraph(label1);
			rotatedParagraph.setRotationAngle(calculateRadiusFromDegree(45f));
			document.add(rotatedParagraph);

			Paragraph borderedParagraph = new Paragraph(label2);
			borderedParagraph.setBorder(new DashedBorder(GREEN, 2f));
			document.add(borderedParagraph);

			Paragraph offsetedParagraph = new Paragraph(label3);
			offsetedParagraph.setMargin(6f);
			offsetedParagraph.setPadding(3f);
			offsetedParagraph.setBorder(new SolidBorder(1f));
			document.add(offsetedParagraph);


		} catch (FileNotFoundException e) {
			log.error("Creating PDF failed", e);
			throw new ITextException(e.getMessage());
		}

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(simplePdf))) {
			String firstPageContent = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
//			assertThat(firstPageContent).contains(label1);
			assertThat(firstPageContent).contains(label2);
			assertThat(firstPageContent).contains(label3);
		}
	}

	private double calculateRadiusFromDegree(Float rotation) {
		// half rotation in Radians is Pi (3.14) -> full rotation is 2 Pi
		return PI / 180 * rotation;
	}

}
