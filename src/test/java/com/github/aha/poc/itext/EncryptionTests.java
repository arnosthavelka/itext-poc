package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.EncryptionConstants.ALLOW_PRINTING;
import static com.itextpdf.kernel.pdf.EncryptionConstants.ENCRYPTION_AES_256;
import static com.itextpdf.kernel.pdf.PdfVersion.PDF_1_7;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@DisplayName("iText security features")
public class EncryptionTests extends AbstractTest {

	public static final String RESULT = RESULT_PATH + "/encryption.pdf";

	@Test
	@DisplayName("add password and restrict (remove) copy permission")
	public void testEncryption() throws Exception {
		
	    PdfReader reader = new PdfReader("hello-source.pdf");
		WriterProperties wp = new WriterProperties();
		wp.setPdfVersion(PDF_1_7);
		wp.setStandardEncryption("aha".getBytes(), null, ALLOW_PRINTING /* | EncryptionConstants.ALLOW_COPY */, ENCRYPTION_AES_256);
		PdfWriter writer = new PdfWriter(RESULT);

		PdfDocument pdf = new PdfDocument(reader, writer);
		Document document = new Document(pdf);
		document.add(new Paragraph("additional text"));
		document.close();
		pdf.close();
	    reader.close();
	}

}
