package com.github.aha.poc.itext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

@DisplayName("Generation of barcode & QR codes")
class BarcodeAndQrTests extends AbstractTest {

	private static Lorem lorem = LoremIpsum.getInstance();

	@Test
	@DisplayName("Add QR code with the label")
	void addQrCode() throws Exception {
		String title = lorem.getWords(3);

		DocumentBuilder documentBuilder = initPdf();
		documentBuilder.addTitle(title);
		documentBuilder.addQrCode("https://github.com/arnosthavelka/itext-poc/");
		documentBuilder.generateDocument();
	}

}
