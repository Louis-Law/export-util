package com.ext.util;

import org.apache.poi.hssf.usermodel.contrib.HSSFCellUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import com.ext.model.Style;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: xiang
 * Date: 2010-6-28
 * Time: 10:23:45
 * To change this template use File | Settings | File Templates.
 */
public class StyleUtil {
    public static final short COLUMN_SIZE  = 1;//列表框大小
    public static final int HEIGHT = 30;
    public static final int WIDTH = 100;
    public static final int FONTSIZE = 12;
    //给合并单元格加样式
    public  static void setRegionStyle(HSSFSheet sheet, CellRangeAddress region )
    {
      
        int rowFrom = region.getFirstRow();
        int rowTo = region.getLastRow();
        int colFrom = region.getFirstColumn();
        int colTo = region.getLastColumn();
        for (int i = rowFrom; i <= rowTo; i ++)
        {
            HSSFRow row = HSSFCellUtil.getRow(i, sheet);

            for (int j = colFrom; j <= colTo; j++)
            {
                HSSFCell cell = HSSFCellUtil.getCell(row, (short)j);
                
                if(j == colFrom&&i==rowFrom)
                {
                  cell.setCellStyle(StyleUtil.getDefaultStyle(sheet.getWorkbook(),cell.getCellStyle()));
                }else{
                     HSSFCellStyle cellStyle =   sheet.getWorkbook().createCellStyle();
                     cellStyle = getDefaultStyle(sheet.getWorkbook(),cellStyle) ;
                     cell.setCellStyle(cellStyle);
                }

            }
        }
    }
     //得到默认样式
    public  static HSSFCellStyle getDefaultStyle(HSSFWorkbook wb,HSSFCellStyle cellStyle)
    {

          cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
          cellStyle.setBorderBottom(COLUMN_SIZE);
          cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
          cellStyle.setBorderLeft(COLUMN_SIZE);
          cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
          cellStyle.setBorderRight(COLUMN_SIZE);
          cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
          cellStyle.setBorderTop(COLUMN_SIZE);
          cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
        return cellStyle;
    }
    //设置单元格背景颜色
    private static HSSFCellStyle setBackGroundColorOfCell(HSSFWorkbook wb,HSSFCellStyle cellStyle,short color)
    {
          
        /* cellStyle.setFillPattern(HSSFCellStyle.ALT_BARS);
         cellStyle.setFillBackgroundColor(HSSFColor.BLUE.index);// 设置单元格的背景颜色．*/
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
          return cellStyle;
    }
    public static  HSSFCellStyle getStyle(HSSFWorkbook wb,HSSFCellStyle style, Style htmlStyle,String [][]attr)
    {
         for(int x=0;x<attr.length;x++)
         {
              if("id".equals(attr[x][0])&&(!"".equals(attr[x][1])))
             {
                 String[][] idstyle  =  htmlStyle.getStyleMap().get("#"+attr[x][1]);
                 if(idstyle!=null)
                 {
                     setStyle (wb,idstyle,style);
                 }

             }
         }

         return style;
    }
    //从html <style>  中得到style填充到cell中
    private  static void setStyle(HSSFWorkbook wb,String[][] astyle,HSSFCellStyle style)
    {
         for(int x=0;x<astyle.length;x++)
         {
              if(Style.BACKGROUNDCOLOR.equals(astyle[x][0])&&(!"".equals(astyle[x][1])))
             {
                 String color = astyle[x][1] ;
                 short indexc = getIndexOfColor(color);
                 if(indexc!=0)
                 {
                      setBackGroundColorOfCell(null,style,indexc);
                 }   

             }else if(Style.FONTSIZE.equals(astyle[x][0])&&(!"".equals(astyle[x][1])))
             {

                    setFontStyle(wb,style,Style.FONTSIZE,astyle[x][1]);

             }else if(Style.FONTFAMILY.equals(astyle[x][0])&&(!"".equals(astyle[x][1])))
             {
//                 String fontFamily = astyle[x][1];
                 

             }
         }
    }
   
    //得到HSSFColor颜色的索引
    @SuppressWarnings("unchecked")
	private static short  getIndexOfColor(String color)
    {
       Hashtable<String, HSSFColor> hashtable = HSSFColor.getTripletHash();
        StringBuffer sb = new StringBuffer();
       if(color.startsWith("#"))
       {
         //color = color.substring()
         color = color.substring(color.indexOf("#")+1,color.length());
         if(color.length()>=6)
         {

             for(int i=0;i<color.length();i++)
             {
                 if(i%2==0)
                 {
                     String a = color.substring(i,i+2);
                     if("00".equals(a))
                     {
                        sb.append("0"); 
                     }else
                     {
                         sb.append(a).append(a);
                     }

                    sb.append(":");
                 }
             }
             
         }
       }
        String c = sb.toString().substring(0,sb.toString().lastIndexOf(":"));
        HSSFColor hssfc = hashtable.get(c.toUpperCase());
        if(hssfc!=null)
        {
           return hssfc.getIndex();
        }
         return 0; //返回0说明没有找到颜色代码索引
    }

    /**
     * 设置行列的高和宽
     */
    public static  void setColumnAndRowHW( HSSFRow hssfrow ,HSSFCell cell ,String [][]attr)
    {
             for(int x=0;x<attr.length;x++)
            {
              if("height".equals(attr[x][0])&&(!"".equals(attr[x][1])))
              {
                   String height = attr[x][1];
                   hssfrow.setHeight((short)(Integer.parseInt(height)*HEIGHT));
                 
              }else if("width".equals(attr[x][0])&&(!"".equals(attr[x][1])))
              {
                   String width = attr[x][1];
                   cell.getSheet().setColumnWidth(cell.getColumnIndex(),Integer.parseInt(width)*WIDTH);
                 
              }
            }
    }                          
     public static void setFontStyle(HSSFWorkbook wb,HSSFCellStyle style,String fontStyleName,String fontStyleValue)
     {
         HSSFFont font=wb.createFont();
         if(fontStyleName.equals(Style.FONTSIZE))
         {
             if(fontStyleValue.endsWith("px"))
             {
                font.setFontHeight((short)(Integer.parseInt(fontStyleValue.substring(0,fontStyleValue.indexOf("px")))*FONTSIZE));
                

             }

         }
         else if(fontStyleName.equals(Style.FONTFAMILY))
         {
             
         }
           style.setFont(font);
    }
}
