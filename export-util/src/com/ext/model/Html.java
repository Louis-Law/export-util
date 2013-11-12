package com.ext.model;

/**
 * Created by IntelliJ IDEA.
 * User: xiang
 * Date: 2010-6-21
 * Time: 13:49:08
 * To change this template use File | Settings | File Templates.
 */
public class Html extends Doc{
    
    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    private Head head;
    private Body body;

}
