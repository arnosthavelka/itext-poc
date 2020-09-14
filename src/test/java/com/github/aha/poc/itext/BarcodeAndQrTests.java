package com.github.aha.poc.itext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

@DisplayName("Generation of barcode & QR codes")
class BarcodeAndQrTests extends AbstractTest {

	private static Lorem lorem = LoremIpsum.getInstance();

	@Test
	@DisplayName("Add Barcode 39")
	void addBarcode39() {
		String title = lorem.getWords(3);

		DocumentBuilder documentBuilder = initPdf();
		documentBuilder.addTitle(title);
		documentBuilder.addBarcode39("A35-8579-78");
		documentBuilder.generateDocument();
	}

	@Test
	@DisplayName("Add Barcode 128")
	void addBarcode128() {
		String title = lorem.getWords(3);

		DocumentBuilder documentBuilder = initPdf();
		documentBuilder.addTitle(title);
		documentBuilder.addBarcode128("https://github.com/arnosthavelka/itext-poc/");
		documentBuilder.generateDocument();
	}

	@Test
	@DisplayName("Add Barcode EAN")
	void addBarcodeEAN() {
		String title = lorem.getWords(3);

		DocumentBuilder documentBuilder = initPdf();
		documentBuilder.addTitle(title);
		documentBuilder.addBarcodeEAN("9788027107339"); // ISBN: 978-80-271-0733-9
		documentBuilder.generateDocument();
	}

	@Test
	@DisplayName("Add QR code with the label")
	void addQrCode() {
		String title = lorem.getWords(3);

		DocumentBuilder documentBuilder = initPdf();
		documentBuilder.addTitle(title);
		documentBuilder.addQrCode("https://github.com/arnosthavelka/itext-poc/");
		documentBuilder.generateDocument();
	}

}
