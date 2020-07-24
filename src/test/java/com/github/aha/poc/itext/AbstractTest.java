package com.github.aha.poc.itext;

import java.io.File;

import org.junit.BeforeClass;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class AbstractTest {
	
	protected static final String RESULT_PATH = "target";
	
	@BeforeClass
	public static void checkOrCreatePath() {
		File f = new File(RESULT_PATH);
		if (!f.exists()) {
			// The directory does not exist.
			f.mkdirs();
		}
	}
	
}
