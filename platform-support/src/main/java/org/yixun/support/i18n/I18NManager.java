package org.yixun.support.i18n; 

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.openkoala.framework.i18n.service.ResourceBundleI18nServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.i18n.NoSuchMessageException;
import com.dayatang.i18n.support.I18nServiceAccessor;

/**
 * I18NManager
 * @author Ken
 * @since 2013-01-11
 *
 */
public class I18NManager {
	
	
	// 国际资源文件存放路径
	private static final String I18N_PATH = "i18n";
	
	// 用于保存basename
	private static Set<String> basename = new HashSet<String>();

	private static ResourceBundleI18nServiceImpl resourceBundleI18nServiceImpl;
	
	private static Logger logger = LoggerFactory.getLogger(I18NManager.class);
	
	private I18NManager() {
		
	}
	
	static {
		init();
	}
	
	/**
	 * 初始化
	 */ 
	private static void init() {
		URL url = Thread.currentThread().getContextClassLoader().getResource(I18N_PATH);
		if (url != null) {

			String urlStr = url.toString();
			// 找到!/ 截断之前的字符串
			String jarPath = urlStr.substring(0, urlStr.indexOf("!/") + 2);

			try {
				URL jarURL = new URL(jarPath);
				JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
				JarFile jarFile = jarCon.getJarFile();
				Enumeration<JarEntry> jarEntrys = jarFile.entries();
				while (jarEntrys.hasMoreElements()) {
					JarEntry entry = jarEntrys.nextElement();
					// 简单的判断路径，如果想做到想Spring，Ant-Style格式的路径匹配需要用到正则。
					String name = entry.getName();
					if(name.startsWith(I18N_PATH) && !entry.isDirectory()){
						if (name.lastIndexOf("properties") != -1) {
							name = name.replace('/','.');
							if (name.indexOf("_") == -1) {
								basename.add(name.substring(0, name.indexOf(".")));
							} else {
								basename.add(name.substring(0, name.indexOf("_")));
							}
						}
					}
				}
			} catch (MalformedURLException e) {
				logger.info(e.getMessage());
			} catch (IOException e) {
				logger.info(e.getMessage());
			}
		}
		// TODO WEB下需要使用/i18n，但普通应用下得使用i18n这种方式，具体原因待查找
		/*
		 * url =
		 * Thread.currentThread().getContextClassLoader().getResource("i18n");
		 * if (url != null) { File file = new File(url.getFile());
		 * handleBasename(file, "i18n"); }
		 */
	}
	
	/**
	 * 递归处理basename
	 * @param file
	 * @param sb
	 */
	private static void handleBasename(File file, String sb) {
		for (File f : file.listFiles()) {
			if (f.getName().lastIndexOf("properties") != -1) {
				if (f.getName().indexOf("_") == -1) {
					basename.add(sb + "." + f.getName().substring(0, f.getName().indexOf(".")));
				} else {
					basename.add(sb + "." + f.getName().substring(0, f.getName().indexOf("_")));
				}
			}
			if (f.isDirectory()) {
				handleBasename(f, sb + "." + f.getName());
			}
		}
	}
	

	/**
	 * 获取国际化信息
	 * @param key
	 * @param locale
	 * @return
	 */
	public static String getMessage(String key, String locale) {
		initResourceBundleI18nServiceImpl();
		resourceBundleI18nServiceImpl.setBasenames(basename.toArray(new String[basename.size()]));
		I18nServiceAccessor accessor = resourceBundleI18nServiceImpl.getAccessor();
		// 如果没有指定locale属性，就用默认的Locale
		if ("".equals(locale) || locale == null) {
			String message = null;
			try {
				message = accessor.getMessage(key, Locale.getDefault());
			} catch (NoSuchMessageException e) {
				// 如果key在资源文件中不存在，就直接把key作为默认值返回
				return accessor.getMessage(key, key);
			}
			return message;
		}
		return accessor.getMessage(key, new Locale(locale));
	}

	private static void initResourceBundleI18nServiceImpl() {
		if (resourceBundleI18nServiceImpl == null) {
			resourceBundleI18nServiceImpl = new ResourceBundleI18nServiceImpl();
		}
	}
	
	/**
	 * 获取国际化信息（使用默认的Locale）
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
		return getMessage(key, null);
	}
	
}
