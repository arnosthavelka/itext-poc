package com.github.aha.poc.itext;

import static com.itextpdf.io.font.constants.StandardFonts.HELVETICA;
import static com.itextpdf.kernel.geom.PageSize.A4;
import static com.itextpdf.kernel.pdf.EncryptionConstants.ALLOW_PRINTING;
import static com.itextpdf.kernel.pdf.EncryptionConstants.ENCRYPTION_AES_256;
import static com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination.createFit;
import static com.itextpdf.layout.properties.TextAlignment.CENTER;
import static com.itextpdf.layout.properties.VerticalAlignment.TOP;
import static java.lang.Math.PI;
import static java.util.Objects.nonNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.Barcode1D;
import com.itextpdf.barcodes.Barcode2D;
import com.itextpdf.barcodes.Barcode39;
import com.itextpdf.barcodes.BarcodeDataMatrix;
import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.barcodes.BarcodeMSI;
import com.itextpdf.barcodes.BarcodePostnet;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class DocumentBuilder {

	@NonNull
	String targetFile;

	Document document;

	public void init(PdfProperties pdfProperties) {
		var pdfDocument = createDocument(targetFile, buildWriterProperties(pdfProperties));
		document = new Document(pdfDocument, A4, false); // avoid immediate flush due to NPE in watermark feature
													     // see https://github.com/itext/itext7/pull/80
	}

	WriterProperties buildWriterProperties(PdfProperties pdfProperties) {
		var wp = new WriterProperties();
		wp.addXmpMetadata();
		if (nonNull(pdfProperties.getPdfVersion())) {
			wp.setPdfVersion(pdfProperties.getPdfVersion());
		}
		if (nonNull(pdfProperties.getUserPassword()) || nonNull(pdfProperties.getOwnerPassword())) {
			wp.setStandardEncryption(pdfProperties.getUserPassword(), pdfProperties.getOwnerPassword(),
					ALLOW_PRINTING /* | EncryptionConstants.ALLOW_COPY */, ENCRYPTION_AES_256);
		}
		return wp;
	}

	public void generateDocument() {
		document.close();
	}

	PdfDocument createDocument(String targetFilename, WriterProperties writerProperties) {
		try {
			return new PdfDocument(createPdfWriter(targetFilename, writerProperties));
		} catch (FileNotFoundException e) {
			log.error("Creating PDF failed", e);
			throw new PdfException(e.getMessage());
		}
	}

	PdfWriter createPdfWriter(String targetFilename, WriterProperties writerProperties) throws FileNotFoundException {
		return new PdfWriter(targetFilename, writerProperties);
	}

	public void addTitle(String title) {
		var titleElement = createStyledParagraph(title, ParagraphStyle.builder().fontName(HELVETICA).fontSize(20f).build());
		titleElement.setBold();
		document.add(titleElement);
		addMetadata(title, null, null, null);
	}

	public void addParagraph(String text) {
		document.add(createParagraph(text));
	}

	public void addParagraph(Paragraph paragraph) {
		document.add(paragraph);
	}

	public void addBarcode39(String code) {
		document.add(create1DBarcode(code, Barcode39.class));
	}

	public void addBarcode128(String code) {
		document.add(create1DBarcode(code, Barcode128.class));
	}

	public void addBarcodeEAN(String code) {
		document.add(create1DBarcode(code, BarcodeEAN.class));
	}

	public void addBarcodeMSI(String code) {
		document.add(create1DBarcode(code, BarcodeMSI.class));
	}

	public void addBarcodePostnet(String code) {
		document.add(create1DBarcode(code, BarcodePostnet.class));
	}

	public void addQrCode(String code) {
		document.add(create2DBarcode(code, BarcodeQRCode.class));
		document.add(createParagraph(code));
	}

	public void addDataMatrix(String code) {
		document.add(create2DBarcode(code, BarcodeDataMatrix.class));
		document.add(createParagraph(code));
	}

	public void addMetadata(String title, String subject, String author, String creator) {
		var documentInfo = document.getPdfDocument().getDocumentInfo();
		if (nonNull(title)) {
			documentInfo.setTitle(title);
		}
		if (nonNull(subject)) {
			documentInfo.setSubject(subject);
		}
		if (nonNull(author)) {
			documentInfo.setAuthor(author);
		}
		if (nonNull(creator)) {
			documentInfo.setCreator(creator);
		}
	}

	public void addCustomMetadadata(@NonNull String key, @NonNull String value) {
		var documentInfo = document.getPdfDocument().getDocumentInfo();
		documentInfo.setMoreInfo(key, value);
	}

	public void addWatermark(String watermark) {
		float fontSize = 100;
		var paragraph = createStyledParagraph(watermark, ParagraphStyle.builder()
				.fontName(HELVETICA)
				.fontSize(fontSize)
				.opacity(0.5f)
				.build());

		for (int i = 1; i <= document.getPdfDocument().getNumberOfPages(); i++) {
			addWatermarkToPage(i, paragraph, fontSize);
		}
	}

	public void addBookmark(String title) {
		var outlines = document.getPdfDocument().getOutlines(false);
		var newOutline = outlines.addOutline(title);
		newOutline.addDestination(createFit(document.getPdfDocument().getLastPage()));
	}

	public Paragraph createParagraph(String text) {
		return new Paragraph(text);
	}

	Paragraph createStyledParagraph(String content, ParagraphStyle paragraphStyle) {
		var paragraph = new Paragraph(content);
		if (nonNull(paragraphStyle.getFontName())) {
			paragraph.setFont(createFont(paragraphStyle.getFontName()));
		}
		if (nonNull(paragraphStyle.getFontSize())) {
			paragraph.setFontSize(paragraphStyle.getFontSize());
		}
		if (nonNull(paragraphStyle.getRotation())) {
			paragraph.setRotationAngle(calculateRadiusFromDegree(paragraphStyle.getRotation()));
		}
		if (nonNull(paragraphStyle.getBorder())) {
			paragraph.setBorder(paragraphStyle.getBorder());
		}
		if (nonNull(paragraphStyle.getMargin())) {
			paragraph.setMargin(paragraphStyle.getMargin());
		}
		if (nonNull(paragraphStyle.getPadding())) {
			paragraph.setPadding(paragraphStyle.getPadding());
		}
		if (nonNull(paragraphStyle.getOpacity())) {
			paragraph.setOpacity(paragraphStyle.getOpacity());
		}
		return paragraph;
	}

	public Text createStyledText(String label, TextStyle textStyle) {
		var text = new Text(label);
		if (nonNull(textStyle.getColor())) {
			text.setFontColor(textStyle.getColor());
		}
		if (nonNull(textStyle.getBackgroundColor())) {
			text.setBackgroundColor(textStyle.getBackgroundColor());
		}
		if (nonNull(textStyle.getFontFamily())) {
				text.setFont(createFont(textStyle.getFontFamily()));
		}
		if (nonNull(textStyle.getFontSize())) {
			text.setFontSize(textStyle.getFontSize());
		}
		if (textStyle.isBold()) {
			text.setBold();
		}
		if (textStyle.isItalic()) {
			text.setItalic();
		}
		if (textStyle.isUnderline()) {
			text.setUnderline();
		}
		if (textStyle.isLineThrough()) {
			text.setLineThrough();
		}
		return text;
	}

	private double calculateRadiusFromDegree(Float rotation) {
		// half rotation in Radians is Pi (3.14) -> full rotation is 2 Pi
		return PI / 180 * rotation;
	}

	PdfFont createFont(String fontFamily) {
		try {
			return createPdfFont(fontFamily);
		} catch (IOException e) {
			throw new PdfException("Font creation failed", e);
		}
	}

	PdfFont createPdfFont(String fontFamily) throws IOException {
		return PdfFontFactory.createFont(fontFamily);
	}

	private void addWatermarkToPage(int pageIndex, Paragraph paragraph, float fontSize) {
		var pdfDoc = document.getPdfDocument();
		var pdfPage = pdfDoc.getPage(pageIndex);
		var pageSize = pdfPage.getPageSizeWithRotation();

		float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
		float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
		float xOffset = fontSize / 2;
		document.showTextAligned(paragraph, x - xOffset, y, pageIndex, CENTER, TOP, (float) calculateRadiusFromDegree(45f));
	}

	private float getPageWidth() {
		return document.getPdfDocument().getFirstPage().getPageSizeWithRotation().getWidth();
	}

	private <C extends Barcode1D> Image create1DBarcode(String code, Class<C> barcodeClass) {
		try {
			var codeObject = barcodeClass.getConstructor(PdfDocument.class).newInstance(document.getPdfDocument());
			codeObject.setCode(code);
			var codeImage = codeObject.createFormXObject(document.getPdfDocument());
			return createCodeImage(codeImage, false);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new PdfException("The creation of Barcode1D class " + barcodeClass.getName() + "failed", e);
		}
	}

	private <C extends Barcode2D> Image create2DBarcode(String code, Class<C> barcodeClass) {
		try {
			var codeObject = barcodeClass.getConstructor(String.class).newInstance(code);
			var codeImage = codeObject.createFormXObject(document.getPdfDocument());
			return createCodeImage(codeImage, true);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new PdfException("The creation of Barcode2D class " + barcodeClass.getName() + "failed", e);
		}
	}

	private Image createCodeImage(PdfFormXObject codeImage, boolean setWidth) {
		var codeQrImage = new Image(codeImage);
		if (setWidth) {
			codeQrImage.setWidth(getPageWidth() / 4);
		}
		return codeQrImage;
	}

}
