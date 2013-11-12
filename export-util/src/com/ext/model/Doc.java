package com.ext.model;

import org.jdom.Namespace;

/**
 * Created by IntelliJ IDEA.
 * User: xiang
 * Date: 2010-6-22
 * Time: 16:23:12
 * To change this template use File | Settings | File Templates.
 */
public class Doc {
    public String[][] getAttr() {
        return attr;
    }

    public void setAttr(String[][] attr) {
        this.attr = attr;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public String getElementText() {
        return elementText;
    }

    public void setElementText(String elementText) {
        this.elementText = elementText;
    }
    //标签属性
    public   String [][] attr ={
		                                      {"id",""},
                                              {"rowspan",""},
		                                      {"colspan",""},
                                              {"height",""},
		                                      {"width",""}
		                                   };
    public String elementName;  //元素名称
    public Namespace namespace;   //命名空间
    public String elementText;   //元素文本

}
