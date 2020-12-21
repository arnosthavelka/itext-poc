package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

@DisplayName("Generation of One-dimensional (1D) barcodes")
class Barcode1DTests extends AbstractTest {

	// https://en.wikipedia.org/wiki/Code_39
	@Test
	void barcode39() throws IOException {
		String title = lorem.getWords(3);
		String targetPdf = RESULT_PATH + "/example-barcode39.pdf";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addBarcode39("A35-8579-78");
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).startsWith(title);
		}
	}

	// https://en.wikipedia.org/wiki/Code_128
	@Test
	void barcode128() throws IOException {
		String title = lorem.getWords(3);
		String targetPdf = RESULT_PATH + "/example-barcode128.pdf";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addBarcode128("https://github.com/arnosthavelka/itext-poc/");
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).startsWith(title);
		}
	}

	// https://en.wikipedia.org/wiki/International_Article_Number
	@Test
	void barcodeEAN() throws IOException {
		String title = lorem.getWords(3);
		String targetPdf = RESULT_PATH + "/example-barcodeEAN.pdf";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addBarcodeEAN("9788027107339"); // ISBN: 978-80-271-0733-9
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).startsWith(title);
		}
	}

	// https://en.wikipedia.org/wiki/MSI_Barcode
	@Test
	void barcodeMSI() throws IOException {
		String title = lorem.getWords(3);
		String targetPdf = RESULT_PATH + "/example-barcodeMSI.pdf";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addBarcodeMSI("9788027107339");
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).startsWith(title);
		}
	}

	// https://en.wikipedia.org/wiki/POSTNET
	@Test
	void barcodePOSTNET() throws IOException {
		String title = lorem.getWords(3);
		String targetPdf = RESULT_PATH + "/example-barcodePOSTNET.pdf";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addBarcodePostnet("9788027107339");
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			assertThat(PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy())).startsWith(title);
		}
	}

}
