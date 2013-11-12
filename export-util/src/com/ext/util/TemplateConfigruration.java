package com.ext.util;

import java.io.File;
import java.util.Locale;
import org.apache.log4j.Logger;

import freemarker.template.Configuration;

public class TemplateConfigruration {
	private static String templateDir = "/templates";
	private static String WebContentPath;
	
	private static Logger log = Logger.getLogger(HTML2PDF.class);
	private static boolean isInit = false;
	private static Configuration cfg ;
	public static void init(String tempPath) {
		if (!isInit) {
			cfg = new Configuration();
			cfg.setEncoding(Locale.CHINA, "UTF-8");// 设置字符集
			String configPath = null;
			if(tempPath != null && !"".equals(tempPath)){
				configPath = tempPath;
			}else{
				WebContentPath = getWebcontentPath();
				configPath = WebContentPath + templateDir;
			}
			try {
				cfg.setDirectoryForTemplateLoading(new File(configPath));
				isInit = true;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("配置模板出错,模板根路径："+ WebContentPath, e);
			}
		}
	}
	public static Configuration getConfiguration(String templatePath){
		init(templatePath);
		return cfg;
	}
	public static String getWebcontentPath(){
		if(WebContentPath == null){
			WebContentPath = HTML2EXCEL.class
					.getResource("/").getFile();
			int index = WebContentPath.lastIndexOf("/classes");
			WebContentPath = WebContentPath.replaceAll("%20", " ");
			if (index > 0) {
				WebContentPath = WebContentPath.substring(0, index);
			} else {
				index = WebContentPath.lastIndexOf("/bin");
				if (index > 0) {
					WebContentPath = WebContentPath.substring(0, index);
				}
			}
		}
		return  WebContentPath;
	}
}
