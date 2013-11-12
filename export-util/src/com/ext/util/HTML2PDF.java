package com.ext.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Map;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HTML2PDF {
	private static Configuration cfg;
	
	public static void init(String templatePath){
		cfg = TemplateConfigruration.getConfiguration(templatePath);
	}
	/**
	 * 
	 * @param data
	 *            填充数据
	 * @param templatePath
	 *            模板名称
	 * @param filePath
	 *            生成文件路径
	 * @return 生成PDF文件的路径
	 * @throws Exception
	 */
	public static String toPDFFile(Map<String, Object> data,
			String templateName, String filePath) throws Exception {

		ITextRenderer render = configPDFRender(data, templateName);
		if(filePath == null){
			filePath = TemplateConfigruration.getWebcontentPath() + "/" + System.currentTimeMillis() + ".pdf";
		}
		FileOutputStream fos = new FileOutputStream(filePath);
		render.createPDF(fos);
		fos.close();
		return filePath;
	}
	public static byte[] toPDFByteArray(Map<String, Object> data,
			String templateName) throws Exception {
		ITextRenderer render = configPDFRender(data, templateName);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		render.createPDF(bos);
		byte[] dataByte = bos.toByteArray();
		return dataByte;
	}

	private static ITextRenderer configPDFRender(Map<String, Object> data,
			String templateName) throws Exception {
		init(null);
		Template tem = cfg.getTemplate(templateName);
		StringWriter writer = new StringWriter();
		tem.process(data, writer);
		String content = writer.toString();
		// 生成pdf
		ITextRenderer render = new ITextRenderer();
		ITextFontResolver fontResolver = render.getFontResolver();
		String curDir = System.getProperty("user.dir");

		/**
		 * simsun.ttc [宋体] 是//C:\WINDOWS\Fonts\里的， 可以把他下面的ttc都拷贝到项目中，想引用哪个就选择哪个,
		 * 本实例程序只拷贝了simsun.ttc到Fonts源码包中
		 */
		String songti = new File(curDir + "/Fonts", "simsun.ttc").toString();
		fontResolver
				.addFont(songti, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		render.setDocumentFromString(content);
		render.layout();
		return render;
	}

//	public static void main(String[] args) throws Exception {
//		templateDir = "/report";
//		// 第二步 填充数据
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//		Map<String, Object> item1 = new HashMap<String, Object>();
//		item1.put("id", "001");
//		item1.put("age", "40");
//		item1.put("name", "张三");
//		item1.put("address", "中南海");
//		list.add(item1);
//		Map<String, Object> item2 = new HashMap<String, Object>();
//		item2.put("id", "002");
//		item2.put("age", "60");
//		item2.put("name", "李四");
//		item2.put("address", "北戴河");
//		list.add(item2);
//		Map<String, Object> item3 = new HashMap<String, Object>();
//		item3.put("id", "002");
//		item3.put("age", "60");
//		item3.put("name", "王五");
//		item3.put("address", "知春路");
//		list.add(item3);
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("items", list);
//		System.out.println(toPDFFile(data, "demo.ftl",null));
//	}
}
