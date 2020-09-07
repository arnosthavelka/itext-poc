package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.EncryptionConstants.ALLOW_PRINTING;
import static com.itextpdf.kernel.pdf.EncryptionConstants.ENCRYPTION_AES_256;
import static com.itextpdf.kernel.pdf.PdfVersion.PDF_2_0;

import java.io.FileNotFoundException;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.renderer.IRenderer;

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
		wp.setPdfVersion(version);
		return wp;
	}

	public void addTitle(String text) {
		Paragraph titleElement = createParagraph(text);
		titleElement.setBold();
		document.add(titleElement);
	}

	public void addParagraph(String text) {
		document.add(createParagraph(text));
	}

	public void addQrCode(String code) {
		Paragraph codeLabel = createParagraph(code);
		BarcodeQRCode codeObject = new BarcodeQRCode(code);
		PdfFormXObject codeImage = codeObject.createFormXObject(document.getPdfDocument());
		Image codeQrImage = new Image(codeImage);
		codeQrImage.setWidth(200);
//		codeQrImage.setWidth(getElementWidth(codeLabel));
		document.add(codeQrImage);
		document.add(codeLabel);
	}

	private float getElementWidth(Paragraph element) {
		IRenderer paragraphRenderer = element.createRendererSubTree();
		return paragraphRenderer.getOccupiedArea().getBBox().getWidth();
//		LayoutResult result = paragraphRenderer.setParent(document.getRenderer()).
//				layout(new LayoutContext(new LayoutArea(1, new Rectangle(1000, 1000))));
//		
//		return result.getOccupiedArea().getBBox().getWidth();
	}

	Paragraph createParagraph(String text) {
		return new Paragraph(text);
	}

}
