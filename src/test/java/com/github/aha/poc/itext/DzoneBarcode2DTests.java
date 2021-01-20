package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.barcodes.BarcodeDataMatrix;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

@DisplayName("Two-dimensional (2D) barcodes")
class DzoneBarcode2DTests {

	final static Logger log = LoggerFactory.getLogger(DzoneBarcode2DTests.class);

	static final String GITHUB_URL = "https://github.com/arnosthavelka/itext-poc/";

	@Test
	void qrBarcode() throws IOException {
		String targetPdf = "target/example-qrcode.pdf";

		try (PdfWriter writer = new PdfWriter(targetPdf);
				PdfDocument pdfDocument = new PdfDocument(writer);
				Document document = new Document(pdfDocument)) {

			var codeObject = new BarcodeQRCode(GITHUB_URL);
			PdfFormXObject codeFormObject = codeObject.createFormXObject(pdfDocument);
			Image codeImage = createCodeImage(codeFormObject);
			document.add(codeImage);

			document.add(new Paragraph(GITHUB_URL));

		} catch (FileNotFoundException e) {
			log.error("PDF creatiion failed", e);
			throw new ITextException(e.getMessage());
		}

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			String pdfContent = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
			assertThat(pdfContent).endsWith(GITHUB_URL);
		}
	}

	@Test
	void datamatrixBarcode() throws IOException {
		String targetPdf = "target/example-datamatrix.pdf";

		try (PdfWriter writer = new PdfWriter(targetPdf);
				PdfDocument pdfDocument = new PdfDocument(writer);
				Document document = new Document(pdfDocument)) {

			document.add(createCodeImage(new BarcodeDataMatrix(GITHUB_URL).createFormXObject(pdfDocument)));

			document.add(new Paragraph(GITHUB_URL));

		} catch (FileNotFoundException e) {
			log.error("PDF creatiion failed", e);
			throw new ITextException(e.getMessage());
		}

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			String pdfContent = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
			assertThat(pdfContent).endsWith(GITHUB_URL);
		}
	}

	private Image createCodeImage(PdfFormXObject codeImage) {
		var codeQrImage = new Image(codeImage);
		codeQrImage.setWidth(100);
		return codeQrImage;
	}

}
