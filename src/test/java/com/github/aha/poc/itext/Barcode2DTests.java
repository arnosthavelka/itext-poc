package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

@DisplayName("Generation of Two-dimensional (2D) codes")
class Barcode2DTests extends AbstractTest {

	@Test
	void qrCode() throws IOException {
		String title = lorem.getWords(3);
		String targetPdf = RESULT_PATH + "/example-qrcode.pdf";
		String qrCodeUrl = "https://github.com/arnosthavelka/itext-poc/";

		DocumentBuilder documentBuilder = preparePdf(targetPdf);
		documentBuilder.addTitle(title);
		documentBuilder.addQrCode(qrCodeUrl);
		documentBuilder.generateDocument();

		try (PdfDocument pdfDocument = new PdfDocument(new PdfReader(targetPdf))) {
			String pdfContent = PdfTextExtractor.getTextFromPage(pdfDocument.getFirstPage(), new LocationTextExtractionStrategy());
			assertThat(pdfContent).startsWith(title);
			assertThat(pdfContent).contains(qrCodeUrl);
		}
	}

}
