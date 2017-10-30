package com.zb.common.constant;

/**
 * Created by Administrator on 2017/4/15.
 */
public enum HttpAttrType {
    CONTENT_TYPE_HTML("content-type", "text/html"),   //HTML格式
    CONTENT_TYPE_PLAIN("content-type", "text/plain"),  //纯文本格式
    CONTENT_TYPE_XML("content-type", "text/xml"),    // XML格式
    CONTENT_TYPE_JSON("content-type", "application/json");//JSON数据格式

    private String key;
    private String value;

    HttpAttrType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public java.lang.String getKey() {
        return key;
    }

    public void setKey(java.lang.String key) {
        this.key = key;
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }
}
