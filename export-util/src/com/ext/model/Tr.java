package com.ext.model;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: xiang
 * Date: 2010-6-21
 * Time: 15:03:02
 * To change this template use File | Settings | File Templates.
 */
public class Tr extends Doc{

    
    public List<Td> getTdlist() {
        return tdlist;
    }

    public void setTdlist(List<Td> tdlist) {
        this.tdlist = tdlist;
    }
    private List<Td> tdlist = null;

    public String getTrIndex() {
        return trIndex;
    }

    public void setTrIndex(String trIndex) {
        this.trIndex = trIndex;
    }

    private String trIndex;

}
