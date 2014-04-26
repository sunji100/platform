package org.yixun.test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;




import javax.mail.internet.MimeUtility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PropertiesTest {
	@Test
	public void read(){
//		RWProperties properties = new RWProperties("test.properties");
//		System.out.println(properties.getProperty("a"));
		URL url = Thread.currentThread().getContextClassLoader().getResource("i18n");
		File file = new File(url.getFile());
		File[] listFiles = file.listFiles();
		System.out.println(url);
	}
	@Test
	public void write(){
//		RWProperties properties = new RWProperties("test.properties");
//		properties.setProperty("a", "1324");
//		try {
//			System.out.println(MimeUtility.encodeText("基团工asd12ads东奔西走.xls", "UTF-8", "B"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
