package com.metaversant.expirable.beans;

import org.alfresco.service.cmr.repository.NodeRef;

import java.util.List;

public interface ReportWriter {
    NodeRef save(List<ReportData> reportDataList);
    String format(ReportData reportData);
    void setName(String reportName);
}
