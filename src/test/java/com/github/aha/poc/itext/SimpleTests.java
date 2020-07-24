package com.github.aha.poc.itext;

import java.io.FileOutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@DisplayName("Basic tests in JUnit5")
public class SimpleTests extends AbstractTest {

	public static final String RESULT = RESULT_PATH + "/hello.pdf";

	@Test
	public void testSimpleHelloWorld() throws Exception {
        // step 1
        Document document = new Document(PageSize.LETTER);
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // default is version 1.4
        writer.setPdfVersion(PdfWriter.VERSION_1_7);
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();		
	}

}
