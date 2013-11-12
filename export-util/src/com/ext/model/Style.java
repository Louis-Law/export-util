package com.ext.model;


import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xiang
 * Date: 2010-6-21
 * Time: 13:59:46
 * To change this template use File | Settings | File Templates.
 */
public class Style extends Doc{

    public  static final String FONTSIZE =  "font-size";
    public  static final String FONTFAMILY="font-family";
    public  static final String BACKGROUNDCOLOR = "background-color" ;

    public Map<String, String[][]> getStyleMap() {
        return styleMap;
    }

    public void setStyleMap(Map<String, String[][]> styleMap) {
        this.styleMap = styleMap;
    }

    private Map<String, String[][]> styleMap;  //<样式标记,样式内容>

}
