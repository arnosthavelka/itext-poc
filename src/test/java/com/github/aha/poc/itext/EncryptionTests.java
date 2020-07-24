package com.github.aha.poc.itext;

import java.io.FileOutputStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

@DisplayName("iText security features")
public class EncryptionTests extends AbstractTest {

	public static final String RESULT = RESULT_PATH + "/encryption.pdf";

	@Test
	@DisplayName("add password and restrict (remove) copy permission")
	public void testEncryption() throws Exception {
		
	    PdfReader reader = new PdfReader("hello-source.pdf");
	    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
	    stamper.setEncryption("aha".getBytes(), null, PdfWriter.ALLOW_PRINTING /*| PdfWriter.ALLOW_COPY */, PdfWriter.ENCRYPTION_AES_128);
	    stamper.close();
	    reader.close();
	}

}
