package org.shoper.commons;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ShawnShoper on 16/10/8.
 */
public class ImageUtilTests {
	@Test
	public void allTest () throws IOException {
		InputStream inputStream = new FileInputStream(new File("src/test/resources/2.jpg"));
		String base64 = ImageUtil.ImageToBase64(inputStream);
		inputStream.close();
		inputStream = new FileInputStream(new File("src/test/resources/2.jpg"));
		ImageInfo imageInfo = ImageUtil.ReadImageToInfo(inputStream);
		System.out.println(base64);
		System.out.println(imageInfo);
	}
}
