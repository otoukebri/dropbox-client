package tn.optile.task.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import tn.optile.task.client.utils.Utils;

@RunWith(JUnit4.class)
public class UtilsTest {

	@Test
	public void testFormatSize() {
		assertEquals("900.00 Bytes", Utils.formatFileSize(900));
		assertEquals("900.00 KB", Utils.formatFileSize(900*1024));
		assertEquals("900.00 MB", Utils.formatFileSize(900*1024*1024));
//		assertEquals("900.00 GB", Utils.formatFileSize(900*1024*1024*1024));
		
	}
	

}
