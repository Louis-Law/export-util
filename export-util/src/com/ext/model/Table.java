package com.ext.model;


import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: xiang
 * Date: 2010-6-21
 * Time: 10:50:06
 * To change this template use File | Settings | File Templates.
 */
public class Table extends Doc{

    
    public List<Tr> getTrlist() {
        return trlist;
    }

    public void setTrlist(List<Tr> trlist) {
        this.trlist = trlist;
    }

    private List<Tr> trlist;
}
