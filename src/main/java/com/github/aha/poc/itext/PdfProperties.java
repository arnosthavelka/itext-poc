package com.github.aha.poc.itext;

import com.itextpdf.kernel.pdf.PdfVersion;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PdfProperties {

	private PdfVersion pdfVersion;
	private byte[] userPassword;
	private byte[] ownerPassword;

}
