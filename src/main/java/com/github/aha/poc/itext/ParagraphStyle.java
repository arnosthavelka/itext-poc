package com.github.aha.poc.itext;

import com.itextpdf.layout.borders.Border;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParagraphStyle {

	private String fontName;
	private Float fontSize;
	private Float rotation;
	private Border border;
	private Float margin;
	private Float padding;
	private Float opacity;

}
