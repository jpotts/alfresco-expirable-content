package com.conexiam.expirable.beans;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;

import java.util.List;

public interface ReportWriter {
    NodeRef save(List<ReportData> reportDataList);
    String format(ReportData reportData);
    void setName(String reportName);
}
