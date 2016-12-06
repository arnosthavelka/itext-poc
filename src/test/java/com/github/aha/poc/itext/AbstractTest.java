package com.github.aha.poc.itext;

import java.io.File;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AbstractTest {
	
	/** Path to the resulting PDF file. */
	protected static final String RESULT_PATH = "target";
	
	/**
	 * Creates a PDF file: hello.pdf
	 * 
	 * @param args
	 *            no arguments needed
	 */
	@BeforeClass
	public static void checkPath() {
		File f = new File(RESULT_PATH);
		if (!f.exists()) {
			// The directory does not exist.
			f.mkdirs();
		}
	}
	
}
