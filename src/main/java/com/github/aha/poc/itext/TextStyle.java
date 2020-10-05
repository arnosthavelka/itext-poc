package com.github.aha.poc.itext;

import com.itextpdf.kernel.colors.Color;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TextStyle {

	private Color color;
	private Color backgroundColor;
	private String fontFamily;
	private boolean bold;
	private boolean italic;
	private boolean underline;
	private boolean lineThrough;

}
