package com.github.aha.poc.itext;

import static com.itextpdf.kernel.pdf.EncryptionConstants.ALLOW_PRINTING;
import static com.itextpdf.kernel.pdf.EncryptionConstants.ENCRYPTION_AES_256;
import static com.itextpdf.kernel.pdf.PdfVersion.PDF_2_0;

import java.io.FileNotFoundException;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DocumentBuilder {

	@NonNull
	String targetFile;

	@Setter
	PdfVersion pdfVersion = PDF_2_0;

	Document document;

	public void init() {
		document = new Document(createDocument(targetFile, buildWriterProperties(pdfVersion)));
	}

	public void initWithPassword(byte[] userPassword, byte[] ownerPassword) {
		WriterProperties writerProperties = buildWriterProperties(pdfVersion);
		writerProperties.setStandardEncryption(userPassword, ownerPassword, ALLOW_PRINTING /* | EncryptionConstants.ALLOW_COPY */,
				ENCRYPTION_AES_256);
		document = new Document(createDocument(targetFile, writerProperties));
	}

	public void generateDocument() {
		document.close();
	}

	PdfDocument createDocument(String targetFilename, WriterProperties writerProperties) {
		try {
			PdfWriter writer = createPdfWriter(targetFilename, writerProperties);
			return new PdfDocument(writer);
		} catch (FileNotFoundException e) {
			log.error("Creating PDF failed", e);
			throw new ITextException(e.getMessage());
		}
	}

	PdfWriter createPdfWriter(String targetFilename, WriterProperties writerProperties) throws FileNotFoundException {
		return new PdfWriter(targetFilename, writerProperties); // NOSONAR
	}

	WriterProperties buildWriterProperties(PdfVersion version) {
		WriterProperties wp = new WriterProperties();
		wp.setPdfVersion(version);
		return wp;
	}

	void addTitle(String text) {
		Paragraph titleElement = new Paragraph(text);
		titleElement.setBold();
		document.add(titleElement);
	}

	void addParagraph(String text) {
		document.add(new Paragraph(text));
	}

}
