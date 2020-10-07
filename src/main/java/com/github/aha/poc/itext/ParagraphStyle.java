package com.github.aha.poc.itext;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ParagraphStyle {

	private String fontName;
	private Float fontSize;
	private Float fontRotation;

}
