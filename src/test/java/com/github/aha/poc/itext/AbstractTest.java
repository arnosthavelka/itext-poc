package com.github.aha.poc.itext;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;

abstract class AbstractTest {

	protected static final String RESULT_PATH = "target";

	public static final String GENERATED_PDF = RESULT_PATH + "/hello.pdf";
	public static final String GENERATED_SECURED_PDF = RESULT_PATH + "/hello-secured.pdf";
	
	@BeforeAll
	public static void checkOrCreatePath() {
		File f = new File(RESULT_PATH);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	protected void generatePDF(String title, String content) {
		DocumentBuilder documentBuilder = new DocumentBuilder(GENERATED_PDF);
		documentBuilder.init();
		addPdfContent(title, content, documentBuilder);
	}

	protected void generateSecuredPDF(String userPassword, String title, String content) {
		DocumentBuilder documentBuilder = new DocumentBuilder(GENERATED_SECURED_PDF);
		documentBuilder.initWithPassword(userPassword.getBytes(), null);
		addPdfContent(title, content, documentBuilder);
	}

	private void addPdfContent(String title, String content, DocumentBuilder documentBuilder) {
		documentBuilder.addTitle(title);
		documentBuilder.addParagraph(content);
		documentBuilder.generateDocument();
	}

}