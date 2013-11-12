package com.ext.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.ext.model.Body;
import com.ext.model.Doc;
import com.ext.model.Head;
import com.ext.model.Html;
import com.ext.model.Style;
import com.ext.model.Table;
import com.ext.model.Td;
import com.ext.model.Title;
import com.ext.model.Tr;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HTML2EXCEL {
	/**
	 * 正则表达式
	 */
	private static final String reg1 = "\\{(.*)\\}";// 匹配{}里内容的
//	private static final String reg2 = "\\#(.*)\\{";// 匹配#与{之间的内容
	private static final String reg3 = "(.*)\\{"; // 匹配{之前的内容
	/**
	 * (.*)\{(.*)\} 表格位置偏移量 ,默认为5,表明表格在(5,5)的坐标地方开始绘制
	 */
	private static final int COL_OFFSET = 0;// 行偏移量
	private static final int ROW_OFFSET = 0;// 列偏移量
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
	 * @return 生成Excel文件的byte数组
	 * @throws Exception
	 */
	public static byte[] toExcelByteArry(Map<String, Object> data,
			String templateName) throws Exception {
		
		HSSFWorkbook wb = configWorkbook(data,templateName);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		wb.write(bos);
		byte[] byteData = bos.toByteArray();
		bos.close();
		return byteData;
	}
	/**
	 * 根据数据，模板，生成HSSFWorkbook
	 * @param data
	 * @param templatePath
	 * @return
	 * @throws Exception
	 */
	private static HSSFWorkbook configWorkbook(Map<String, Object> data,
			String templatePath) throws Exception{
		init(null);
		// 利用freemarker得到模板文本字符串
		Template tem = cfg.getTemplate(templatePath);
		StringWriter writer = new StringWriter();
		tem.process(data, writer);
		String content = writer.toString();

		// 利用jdom解析字符串转换成html对象
		Document doc = getDoc(content);
		Html html = parseHtml(doc);
		html = renewHtml(html);
		//生成excel
		HSSFWorkbook wb = new HSSFWorkbook();
		wb = html2Excel(wb, html, 0);
		return wb;
	}
	/**
	 * 
	 * @param data 导出数据
	 * @param templatePath 模板名
	 * @param filePath 生成文件的全路径，若为null，则会在程序运行目录下创建文件，
	 * @return
	 * @throws Exception
	 */
	public static String toExcelByteFile(Map<String, Object> data,
			String templateName,String filePath) throws Exception{
		HSSFWorkbook wb = configWorkbook(data,templateName);
		if(filePath == null){
			filePath = TemplateConfigruration.getWebcontentPath() +"/"+ System.currentTimeMillis() + ".xls";
		}
		FileOutputStream fos = new FileOutputStream(filePath);
		wb.write(fos);
		fos.close();
		return filePath;
	}

	// html对象转换成HSSFWorkbook
	public static HSSFWorkbook html2Excel(HSSFWorkbook wb, Html html, int i)
			throws Exception {

		HSSFSheet sheet = wb.createSheet(html.getHead().getTitle().getElementText());

		// sheet.setColumnWidth(5,14*36);
		Style htmlStyle = html.getHead().getStyle();
		Table htmlTable = html.getBody().getTable();
		List<CellRangeAddress> cras = new ArrayList<CellRangeAddress>();

		for (int row = 0; row < htmlTable.getTrlist().size(); row++) {
			Tr tr = htmlTable.getTrlist().get(row);
			// String[][] trattr = tr.getAttr();
			HSSFRow hssfrow = sheet.createRow(row + ROW_OFFSET);
			for (int col = 0; col < tr.getTdlist().size(); col++) {
				Td td = tr.getTdlist().get(col);
				if (td.getAddress() != null) {
					int[] address = new int[2];
					address[0] = td.getAddress()[0] + ROW_OFFSET;
					address[1] = td.getAddress()[1] + COL_OFFSET;
					td.setAddress(address);
				}

				/**
				 * 检查是否有单元格，若有就进行合并
				 */
				CellRangeAddress cra = mergeRegion(td);
				if (cra != null) {
					cras.add(cra);
				}

				if (td.getElementName() != null) {
					HSSFCellStyle cellStyle = wb.createCellStyle();
					HSSFCell hssfcell = hssfrow.createCell(col + COL_OFFSET);
					hssfcell.setCellValue(td.getElementText());
					HSSFCellStyle defaultStyle = StyleUtil.getDefaultStyle(wb,
							cellStyle);
					cellStyle = StyleUtil.getStyle(wb, defaultStyle, htmlStyle,
							td.getAttr());
					StyleUtil
							.setColumnAndRowHW(hssfrow, hssfcell, td.getAttr());
					if (cellStyle != null) {

						hssfcell.setCellStyle(cellStyle);

					} else {
						hssfcell.setCellStyle(defaultStyle);

					}

				}

			}
		}

		for (int cranum = 0; cranum < cras.size(); cranum++) {
			sheet.addMergedRegion(cras.get(cranum));
			StyleUtil.setRegionStyle(sheet, cras.get(cranum));
		}
		sheet = null;
		return wb;
	}

	private static Html renewHtml(Html html) {
		Table htmlTable = html.getBody().getTable();
		for (int row = 0; row < htmlTable.getTrlist().size(); row++) {
			Tr tr = htmlTable.getTrlist().get(row);
			for (int col = 0; col < tr.getTdlist().size(); col++) {
				Td td = tr.getTdlist().get(col);
				if (td.getElementName() != null) {
					int newAddress[] = new int[2];
					newAddress[0] = row;
					newAddress[1] = col;
					td.setAddress(newAddress);
					renewHtml(html, td);

				}
			}
		}
		return html;
	}

	private static void renewHtml(Html html, Td td) {
		String attr[][] = td.getAttr();
//		int flag = 0;
		Table table = html.getBody().getTable();
		int rowspan = 0;
		int colspan = 0;
		for (int x = 0; x < attr.length; x++) {
			if ("rowspan".equals(attr[x][0]) && (!"".equals(attr[x][1]))) {
				rowspan = Integer.parseInt(attr[x][1]);

			} else if ("colspan".equals(attr[x][0]) && (!"".equals(attr[x][1]))) {

				colspan = Integer.parseInt(attr[x][1]);
			}

		}
		if (rowspan >= 2 && colspan >= 2) {
			int row = td.getAddress()[0];
			int col = td.getAddress()[1];
			for (int trIndex = row; trIndex < row + rowspan; trIndex++) {
				if (trIndex == row) {
					for (int tdIndex = col; tdIndex < col + colspan - 1; tdIndex++) {
						Tr tr = table.getTrlist().get(trIndex);
						Td newtd = new Td();
						tr.getTdlist().add(tdIndex + 1, newtd);

					}
				} else {
					for (int tdIndex = col; tdIndex < col + colspan; tdIndex++) {
						Tr tr = table.getTrlist().get(trIndex);
						Td newtd = new Td();
						int size = tr.getTdlist().size();
						if (size < tdIndex) {
							for (int xy = 0; xy < tdIndex - size; xy++) {
								tr.getTdlist().add(size + xy, new Td());
							}
						}
						tr.getTdlist().add(tdIndex, newtd);
						newtd = null;
					}
				}

			}

		} else if (rowspan >= 2) {
			int row = td.getAddress()[0];
			int col = td.getAddress()[1];
			for (int trIndex = row; trIndex < row + rowspan; trIndex++) {
				if (trIndex != row) {

					Tr tr = table.getTrlist().get(trIndex);
					Td newtd = new Td();
					int size = tr.getTdlist().size();
					if (size < col) {
						for (int xy = 0; xy < col - size; xy++) {
							tr.getTdlist().add(size + xy, new Td());
						}
					}
					tr.getTdlist().add(col, newtd);
					newtd = null;
				}

			}

		} else if (colspan >= 2) {
			int row = td.getAddress()[0];
			int col = td.getAddress()[1];
			for (int tdIndex = col; tdIndex < col + colspan - 1; tdIndex++) {
				Tr tr = table.getTrlist().get(row);

				Td newtd = new Td();
				tr.getTdlist().add(tdIndex + 1, newtd);
				newtd = null;

			}

		}
	}

	// 计算单元格位置并合并单元格
	private static CellRangeAddress mergeRegion(Doc doc) {
		String attr[][] = doc.getAttr();
		Td td = (Td) doc;
		CellRangeAddress cra = null;
		int rowspan = 0;
		int colspan = 0;

		for (int x = 0; x < attr.length; x++) {
			if ("rowspan".equals(attr[x][0]) && (!"".equals(attr[x][1]))) {
				rowspan = Integer.parseInt(attr[x][1]);

			} else if ("colspan".equals(attr[x][0]) && (!"".equals(attr[x][1]))) {
				colspan = Integer.parseInt(attr[x][1]);
			}

		}

		if (rowspan >= 2 && colspan >= 2) {
			cra = new CellRangeAddress(td.getAddress()[0], td.getAddress()[0]
					+ rowspan - 1, td.getAddress()[1], td.getAddress()[1]
					+ colspan - 1);

		} else if (rowspan >= 2) {
			cra = new CellRangeAddress(td.getAddress()[0], td.getAddress()[0]
					+ rowspan - 1, td.getAddress()[1], td.getAddress()[1]);
		} else if (colspan >= 2) {
			cra = new CellRangeAddress(td.getAddress()[0], td.getAddress()[0],
					td.getAddress()[1], td.getAddress()[1] + colspan - 1);
		}

		return cra;
	}

	public static Document getDoc(String xml) {
		// 创建一个新的字符串
		StringReader read = new StringReader(xml);
		// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
		InputSource source = new InputSource(read);
		// 创建一个新的SAXBuilder
		SAXBuilder sb = new SAXBuilder();
		try {
			Document doc = sb.build(source);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	private static Html parseHtml(Document doc) {
		Element ehtml = doc.getRootElement();
		Namespace htmlns = ehtml.getNamespace();
		Element ehead = ehtml.getChild("head", htmlns);
		Head head = new Head();

		Namespace headns = ehead.getNamespace();
		Element ebody = ehtml.getChild("body", htmlns);
		Namespace bodyns = ebody.getNamespace();
		Body body = new Body();
		Html html = new Html();

		/************* style *********************/
		Element estyle = ehead.getChild("style", headns);
		Namespace stylens = estyle.getNamespace();
		Style style = new Style();

		/************* title *********************/
		Element etitle = ehead.getChild("title", headns);
		Namespace titlens = etitle.getNamespace();
		Title title = new Title();

		/************* table *********************/
		Element etable = ebody.getChild("table", bodyns);
		Namespace tablens = etable.getNamespace();
		Table table = new Table();

		/************* tr *********************/
		List<Element> etrList = etable.getChildren("tr", tablens);
		List<Tr> trList = new ArrayList<Tr>();

		for (int t = 0; t < etrList.size(); t++) {
			Element etr = etrList.get(t);
			Tr tr = new Tr();
			tr.setElementName("tr");
			tr.setElementText(etr.getText());
			tr.setNamespace(etr.getNamespace());
			List<Attribute> curAttr = etr.getAttributes();
			String[][] initAttr = tr.getAttr();
			tr.setAttr(getAttr(initAttr, curAttr));
			tr.setTdlist(getTdlist(etr, t));
			tr.setTrIndex(String.valueOf(t));
			trList.add(tr);
			tr = null;
		}
		String[][] initAttr = new Doc().getAttr();
		// 填充table
		List<Attribute> curTableAttr = etable.getAttributes();
		table.setTrlist(trList);
		table.setElementName("table");
		table.setElementText(etable.getText());
		table.setNamespace(tablens);
		table.setAttr(getAttr(initAttr, curTableAttr));

		// 填充body
		List<Attribute> curBodyAttr = ebody.getAttributes();
		body.setTable(table);
		body.setAttr(getAttr(initAttr, curBodyAttr));
		body.setElementName("body");
		body.setElementText(ebody.getText());
		body.setNamespace(ebody.getNamespace());

		// 填充style
		List<Attribute> curStyleAttr = estyle.getAttributes();
		style.setElementName("style");
		style.setElementText(estyle.getText());
		style.setNamespace(stylens);
		style.setAttr(getAttr(initAttr, curStyleAttr));

		style.setStyleMap(parseStyle(estyle.getText().replace("}", "},")));
		// 填充title
		List<Attribute> curTitleAttr = etitle.getAttributes();
		title.setElementName("title");
		title.setElementText(clear(etitle.getText(), "\\n"));
		title.setNamespace(titlens);
		title.setAttr(getAttr(initAttr, curTitleAttr));
		// 填充head
		List<Attribute> curHeadAttr = ehead.getAttributes();
		head.setElementName("head");
		head.setElementText(ehead.getText());
		head.setNamespace(headns);
		head.setStyle(style);
		head.setTitle(title);
		head.setAttr(getAttr(initAttr, curHeadAttr));
		// 填充html
		List<Attribute> curHtmlAttr = ehtml.getAttributes();
		html.setElementName("html");
		html.setElementText(ehtml.getText());
		html.setNamespace(htmlns);
		html.setBody(body);
		html.setHead(head);
		html.setAttr(getAttr(initAttr, curHtmlAttr));
		return html;

	}

	// 获得每个元素的属性
	private static String[][] getAttr(String[][] initAttr,
			List<Attribute> curAttr) {
		for (int tt = 0; tt < curAttr.size(); tt++) {
			String attrName = curAttr.get(tt).getName();
			String attrValue = curAttr.get(tt).getValue();
			for (int attr = 0; attr < initAttr.length; attr++) {
				if (attrName.equals(initAttr[attr][0])) {
					initAttr[attr][1] = attrValue;
				}
			}

		}
		return initAttr;
	}

	@SuppressWarnings("unchecked")
	private static List<Td> getTdlist(Element tr, int trIndex) {
		List<Td> tdlist = new ArrayList<Td>();
		List<Element> etdList = tr.getChildren("td", tr.getNamespace());
		for (int tdn = 0; tdn < etdList.size(); tdn++) {

			Element etd = etdList.get(tdn);
			Td td = new Td();
			List<Attribute> curAttr = etd.getAttributes();
			String[][] initAttr = td.getAttr();
			td.setAttr(getAttr(initAttr, curAttr));
			td.setElementName("td");
			td.setNamespace(etd.getNamespace());
			td.setElementText(etd.getText());
			int[] address = new int[2];
			address[0] = trIndex;
			address[1] = tdn;
			td.setAddress(address);
			tdlist.add(td);

		}
		return tdlist;
	}

	// 替换内容为""
	private static String clear(String text, String regex) {
		text = text.trim();
		text = text.replaceAll(regex, "");
		return text;
	}

	/**
	 * 解析<HEAD></HEAD>中的<STYLE></STYLE>
	 * 
	 * @param styleText
	 * @return
	 */
	private static Map<String, String[][]> parseStyle(String styleText) {
		String[] styleArray = styleText.split(",");
		Map<String, String[][]> styleMap = null;
		if (styleArray.length != 0) {
			styleMap = new HashMap<String, String[][]>();
			String[][] styleContentArray = null;
			for (int i = 0; i < styleArray.length; i++) {
				String style = styleArray[i].trim();
				String styleContent = getPatternValue(style, reg1, 1);
				String styleName = getPatternValue(style, reg3, 1);
				if (styleContent != null) {
					String[] attrArray = styleContent.split(";");
					styleContentArray = new String[attrArray.length][2];
					for (int x = 0; x < attrArray.length; x++) {
						String attrs = attrArray[x];
						String[] attr = attrs.split(":");
						for (int y = 0; y < attr.length; y++) {
							styleContentArray[x][y] = attr[y].trim();
						}
					}
					styleMap.put(styleName, styleContentArray);
				}

			}

		}
		return styleMap;
	}

	// 通过正则表达式获得匹配的值
	private static String getPatternValue(String src, String pattern, int p) {
		String res = null;
		// 创建一个模式
		Pattern pat = Pattern.compile(pattern, Pattern.MULTILINE
				| Pattern.CASE_INSENSITIVE);
		// 根据创建的模式建立一个配置器
		Matcher matcher = pat.matcher(src.replace("\n", ""));
		// 根据配置器匹配到类容
		if (matcher.find()) {
			res = matcher.group(p);
		}
		if (res != null) {
			return res.trim();
		}
		return res;
	}
}
