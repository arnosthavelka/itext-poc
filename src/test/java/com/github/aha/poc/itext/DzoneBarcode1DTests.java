package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.Barcode1D;
import com.itextpdf.barcodes.Barcode39;
import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.barcodes.BarcodeMSI;
import com.itextpdf.barcodes.BarcodePostnet;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

@DisplayName("One-dimensional (1D) barcodes")
class DzoneBarcode1DTests {

	final static Logger log = LoggerFactory.getLogger(DzoneBarcode1DTests.class);

	@Test
	void barcode39(TestInfo ti) throws IOException {
		// https://en.wikipedia.org/wiki/Code_39
		generateBarcode(ti.getTestMethod().get().getName(), "A35-8579-78", Barcode39.class);
	}

	@Test
	void barcode128(TestInfo ti) throws IOException {
		// https://en.wikipedia.org/wiki/Code_128
		generateBarcode(ti.getTestMethod().get().getName(), "https://github.com/arnosthavelka/itext-poc/", Barcode128.class);
	}

	@Test
	void barcodeEAN(TestInfo ti) throws IOException {
		// https://en.wikipedia.org/wiki/International_Article_Number
		generateBarcode(ti.getTestMethod().get().getName(), "8590345410081", BarcodeEAN.class);
	}

	@Test
	void barcodeMSI(TestInfo ti) throws IOException {
		// https://en.wikipedia.org/wiki/MSI_Barcode
		generateBarcode(ti.getTestMethod().get().getName(), "9788027107339", BarcodeMSI.class);
	}

	@Test
	void barcodePOSTNET(TestInfo ti) throws IOException {
		// https://en.wikipedia.org/wiki/POSTNET
		generateBarcode(ti.getTestMethod().get().getName(), "9788027107339", BarcodePostnet.class);
	}

	private <C extends Barcode1D> void generateBarcode(String barcodeType, String barcodeValue, Class<C> barcodeClass) throws IOException {
		String targetPdf = "target/example-" + barcodeType + ".pdf";

		try (PdfWriter writer = new PdfWriter(targetPdf);
				PdfDocument pdfDocument = new PdfDocument(writer);
				Document document = new Document(pdfDocument)) {

			document.add(new Paragraph(barcodeType));
			document.add(create1DBarcode(pdfDocument, barcodeValue, barcodeClass));

		} catch (FileNotFoundException e) {
			log.error("Creating PDF failed", e);
			throw new ITextException(e.getMessage());
		}

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			String pdfContent = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
			assertThat(pdfContent).startsWith(barcodeType);
		}
	}

	private <C extends Barcode1D> Image create1DBarcode(PdfDocument pdfDocument, String code, Class<C> barcodeClass) {
		try {
			var codeObject = barcodeClass.getConstructor(PdfDocument.class).newInstance(pdfDocument);
			codeObject.setCode(code);
			return new Image(codeObject.createFormXObject(pdfDocument));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new ITextException("The creation of Barcode1D class " + barcodeClass.getName() + "failed", e);
		}
	}

}
