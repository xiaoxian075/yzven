package com.at.frame.entity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReqInfo {
    private String url;
    private int ctrlId;
    private int methodId;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCtrlId() {
        return ctrlId;
    }

    public void setCtrlId(int ctrlId) {
        this.ctrlId = ctrlId;
    }

    public int getMethodId() {
        return methodId;
    }

    public void setMethodId(int methodId) {
        this.methodId = methodId;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        response.setHeader("Content-Type","text/plain");
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}