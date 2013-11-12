package com.ext.model;

/**
 * Created by IntelliJ IDEA.
 * User: xiang
 * Date: 2010-6-21
 * Time: 13:54:47
 * To change this template use File | Settings | File Templates.
 */
public class Head extends Doc{


    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
    private Style style;
    private Title title;


    


    
}
