package com.github.aha.poc.itext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

@DisplayName("Generation of barcode & QR code")
class BarcodeAndQrTests extends AbstractTest {

	private static Lorem lorem = LoremIpsum.getInstance();

	@Test
	@DisplayName("Load page content as single (whole) text")
	void loadPageContentAsValue() throws Exception {
		String title = lorem.getWords(3);

		DocumentBuilder documentBuilder = initPdf();
		documentBuilder.addTitle(title);
		documentBuilder.addQrCode("https://github.com/arnosthavelka/itext-poc/");
		documentBuilder.generateDocument();

	}

}
