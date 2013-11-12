package com.ext.model;

/**
 * Created by IntelliJ IDEA.
 * User: xiang
 * Date: 2010-6-21
 * Time: 15:18:02
 * To change this template use File | Settings | File Templates.
 */
public class Td extends Doc{


    public int[] getAddress() {
        return address;
    }

    public void setAddress(int[] address) {
        this.address = address;
    }

    private int [] address; //td所处的位置，[0]代表哪一行 ,[1]代表哪一列
    
}
