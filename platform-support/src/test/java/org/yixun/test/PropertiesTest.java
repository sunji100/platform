package org.yixun.test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;








import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import javax.mail.internet.MimeUtility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.security.core.codec.Base64;

import com.sdicons.json.validator.impl.predicates.Str;
import com.sun.mail.util.BASE64EncoderStream;

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
//		String a = "您好鞢进行硒鼓或东奔西跑地";
//		byte[] encode = Base64.encode(a.getBytes());
//		
//		System.out.println(new String(encode));
//		
//		String s = new String(encode);
//		
//		System.out.println(new String(Base64.decode(s.getBytes())));
//		
//		try {
//			String encode2 = URLEncoder.encode("您好鞢进行硒鼓或东奔西跑地","utf-8");
//			byte[] encode3 = Base64.encode(encode2.getBytes());
//			String string = new String(encode3);
//			System.out.println(new String(encode3));
//			
//			byte[] decode = Base64.decode(string.getBytes());
//			String string2 = new String(decode);
//			System.out.println(URLDecoder.decode(string2,"utf-8"));
//			
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println(Pattern.compile("^(add|insert)").matcher("insertadsfinsert").find());
		
	}
}
