package com.conexiam.expirable.beans;

import java.util.Date;

public class ReportData {
    private String nodeRef;
    private String name;
    private String path;
    private Date expirationDate;

    public ReportData() {
    }

    public ReportData(String nodeRef, String name, String path, Date expirationDate) {
        this.nodeRef = nodeRef;
        this.name = name;
        this.path = path;
        this.expirationDate = expirationDate;
    }

    public String getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(String nodeRef) {
        this.nodeRef = nodeRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
