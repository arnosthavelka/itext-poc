package com.github.aha.poc.itext;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.kernel.utils.CompareTool.CompareResult;

class ComparingTests extends AbstractPdfTest {

	private static final String SIMPLE_PDF = "simple.pdf";

	@Test
	void comparePdfByCatalog() throws Exception {
		var title = "viris tantas prompta";
		var content = "Sententiaeeum repudiare tale.  Sapientemcum dicant hac vel tale nominavi dui in graecis curabitur definiebas quod torquent nunc ullamcorper invidunt mutat possit definitionem.  Nihillobortis aliquid utamur similique mucius praesent in wisi tortor inani latine ocurreret epicurei inceptos eu magnis mollis detraxit.  Persiusaffert pretium erat ex.  Curaemea blandit penatibus cum.  ";

		generatePDF(title, content);

		try (
				var sourcePdfDocument = new PdfDocument(new PdfReader(SIMPLE_PDF));
				var generatedPdfDocument = new PdfDocument(new PdfReader(GENERATED_PDF))) {
			CompareResult result = new CompareTool().compareByCatalog(generatedPdfDocument, sourcePdfDocument);

			assertThat(result.isOk()).isTrue();
		}
	}

	@Test
	void comparePdfByContent() throws Exception {
		var title = "viris tantas prompta";
		var content = "Sententiaeeum repudiare tale.  Sapientemcum dicant hac vel tale nominavi dui in graecis curabitur definiebas quod torquent nunc ullamcorper invidunt mutat possit definitionem.  Nihillobortis aliquid utamur similique mucius praesent in wisi tortor inani latine ocurreret epicurei inceptos eu magnis mollis detraxit.  Persiusaffert pretium erat ex.  Curaemea blandit penatibus cum.  ";

		generatePDF(title, content);

		try (
				var sourcePdfDocument = new PdfDocument(new PdfReader(SIMPLE_PDF));
				var generatedPdfDocument = new PdfDocument(new PdfReader(GENERATED_PDF))) {
			String sourceFile = RESULT_PATH + File.separator + "test-classes" + File.separator + SIMPLE_PDF;
			String tmpDir = RESULT_PATH + File.separator + "pdf_images";

			var result = new CompareTool().compareByContent(GENERATED_PDF, sourceFile, tmpDir);

			assertThat(result).isNull();
		}
	}

}
