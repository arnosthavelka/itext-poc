package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.EncryptionConstants.ALLOW_PRINTING;
import static com.itextpdf.kernel.pdf.EncryptionConstants.ENCRYPTION_AES_256;
import static com.itextpdf.kernel.pdf.PdfVersion.PDF_2_0;
import static java.util.Objects.nonNull;

import java.io.FileNotFoundException;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.Barcode39;
import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DocumentBuilder {

	@NonNull
	String targetFile;

	@Setter
	PdfVersion pdfVersion = PDF_2_0;

	Document document;

	public void init() {
		document = new Document(createDocument(targetFile, buildWriterProperties(pdfVersion)));
	}

	public void initWithPassword(byte[] userPassword, byte[] ownerPassword) {
		WriterProperties writerProperties = buildWriterProperties(pdfVersion);
		writerProperties.setStandardEncryption(userPassword, ownerPassword, ALLOW_PRINTING /* | EncryptionConstants.ALLOW_COPY */,
				ENCRYPTION_AES_256);
		document = new Document(createDocument(targetFile, writerProperties));
	}

	public void generateDocument() {
		document.close();
	}

	PdfDocument createDocument(String targetFilename, WriterProperties writerProperties) {
		try {
			PdfWriter writer = createPdfWriter(targetFilename, writerProperties);
			return new PdfDocument(writer);
		} catch (FileNotFoundException e) {
			log.error("Creating PDF failed", e);
			throw new ITextException(e.getMessage());
		}
	}

	PdfWriter createPdfWriter(String targetFilename, WriterProperties writerProperties) throws FileNotFoundException {
		return new PdfWriter(targetFilename, writerProperties); // NOSONAR
	}

	WriterProperties buildWriterProperties(PdfVersion version) {
		WriterProperties wp = new WriterProperties();
		wp.addXmpMetadata();
		wp.setPdfVersion(version);
		return wp;
	}

	public void addTitle(String text) {
		Paragraph titleElement = createParagraph(text);
		titleElement.setBold();
		document.add(titleElement);
		addMetadata(text, null, null, null);
	}

	public void addParagraph(String text) {
		document.add(createParagraph(text));
	}

	public void addBarcode39(String code) {
		document.add(createBarcode39Image(code));
	}

	public void addBarcode128(String code) {
		document.add(createBarcode128Image(code));
	}

	public void addBarcodeEAN(String code) {
		document.add(createBarcodeEANImage(code));
	}

	public void addQrCode(String code) {
		document.add(createQrCodeImage(code));
		document.add(createParagraph(code));
	}

	public void addMetadata(String title, String subject, String author, String creator) {
		PdfDocumentInfo documentInfo = document.getPdfDocument().getDocumentInfo();
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
		PdfDocumentInfo documentInfo = document.getPdfDocument().getDocumentInfo();
		documentInfo.setMoreInfo(key, value);
	}

	public void addWatermark(String watermark) throws Exception {
		PdfDocument pdfDoc = document.getPdfDocument();

		int fontSize = 100;
		PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		Paragraph paragraph = new Paragraph(watermark)
				.setFont(font)
				.setFontSize(fontSize);

		PdfExtGState gs1 = new PdfExtGState().setFillOpacity(0.5f);

		// Implement transformation matrix usage in order to scale image
		for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {

			PdfPage pdfPage = pdfDoc.getPage(i);
			Rectangle pageSize = pdfPage.getPageSizeWithRotation();

			float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
			float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
			PdfCanvas over = new PdfCanvas(pdfDoc.getPage(i));
			over.saveState();
			over.setExtGState(gs1);
			document.showTextAligned(paragraph, x - fontSize / 2, y, i, TextAlignment.CENTER, VerticalAlignment.TOP, 45);
			over.restoreState();
		}

	}

	private float getPageWidth() {
		return document.getPdfDocument().getFirstPage().getPageSizeWithRotation().getWidth();
	}

	Paragraph createParagraph(String text) {
		return new Paragraph(text);
	}

	Image createBarcode39Image(String code) {
		Barcode39 codeObject = new Barcode39(document.getPdfDocument());
		codeObject.setCode(code);
		PdfFormXObject codeImage = codeObject.createFormXObject(document.getPdfDocument());
		return createCodeImage(codeImage, false);
	}

	Image createBarcode128Image(String code) {
		Barcode128 codeObject = new Barcode128(document.getPdfDocument());
		codeObject.setCode(code);
		PdfFormXObject codeImage = codeObject.createFormXObject(document.getPdfDocument());
		return createCodeImage(codeImage, false);
	}

	Image createBarcodeEANImage(String code) {
		BarcodeEAN codeObject = new BarcodeEAN(document.getPdfDocument());
		codeObject.setCode(code);
		PdfFormXObject codeImage = codeObject.createFormXObject(document.getPdfDocument());
		return createCodeImage(codeImage, false);
	}

	Image createQrCodeImage(String code) {
		BarcodeQRCode codeObject = new BarcodeQRCode(code);
		PdfFormXObject codeImage = codeObject.createFormXObject(document.getPdfDocument());
		return createCodeImage(codeImage, true);
	}

	private Image createCodeImage(PdfFormXObject codeImage, boolean setWidth) {
		Image codeQrImage = new Image(codeImage);
		if (setWidth) {
			codeQrImage.setWidth(getPageWidth() / 4);
		}
		return codeQrImage;
	}

}
