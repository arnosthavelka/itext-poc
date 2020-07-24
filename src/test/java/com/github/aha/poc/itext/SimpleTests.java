package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.PdfVersion.PDF_1_7;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@DisplayName("simple PDF generation")
public class SimpleTests extends AbstractTest {

	public static final String RESULT = RESULT_PATH + "/hello.pdf";

	@Test
	public void testSimpleHelloWorld() throws Exception {
		Document document = new Document(createNewDocument(RESULT));
		document.add(new Paragraph("Hello World by iText 7!"));
		document.close();
	}

	private PdfDocument createNewDocument(String targetFilename) throws FileNotFoundException {
		WriterProperties wp = new WriterProperties();
		wp.setPdfVersion(PDF_1_7);
		PdfWriter writer = new PdfWriter(targetFilename, wp);
		PdfDocument pdf = new PdfDocument(writer);
		return pdf;
	}

}
