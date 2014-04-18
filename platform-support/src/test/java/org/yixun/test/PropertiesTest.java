package org.yixun.test;

import java.io.File;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.yixun.support.properties.util.RWProperties;

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
	}
}
