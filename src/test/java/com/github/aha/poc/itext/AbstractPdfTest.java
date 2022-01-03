package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.PdfVersion.PDF_2_0;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;

abstract class AbstractPdfTest {

	protected static final String RESULT_PATH = "target";

	public static final String GENERATED_PDF = RESULT_PATH + "/hello.pdf";
	
	protected static Lorem lorem = LoremIpsum.getInstance();

	@BeforeAll
	public static void checkOrCreatePath() {
		File f = new File(RESULT_PATH);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	protected void generatePDF(String title, String content) {
		DocumentBuilder documentBuilder = preparePdf();
		addPdfContent(title, content, documentBuilder);
	}

	protected DocumentBuilder preparePdf() {
		return preparePdf(GENERATED_PDF);
	}

	protected DocumentBuilder preparePdf(String targetFile) {
		DocumentBuilder documentBuilder = new DocumentBuilder(targetFile);
		documentBuilder.init(PdfProperties.builder().pdfVersion(PDF_2_0).build());
		return documentBuilder;
	}

	protected void addPdfContent(String title, String content, DocumentBuilder documentBuilder) {
		documentBuilder.addTitle(title);
		documentBuilder.addParagraph(content);
		documentBuilder.generateDocument();
	}

}