package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.PdfVersion.PDF_1_7;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;

import com.itextpdf.kernel.pdf.WriterProperties;

abstract class AbstractTest {
	
	protected static final String RESULT_PATH = "target";
	
	@BeforeAll
	public static void checkOrCreatePath() {
		File f = new File(RESULT_PATH);
		if (!f.exists()) {
			// The directory does not exist.
			f.mkdirs();
		}
	}

	protected WriterProperties buildWriterProperties() {
		WriterProperties wp = new WriterProperties();
		wp.setPdfVersion(PDF_1_7);
		return wp;
	}

}