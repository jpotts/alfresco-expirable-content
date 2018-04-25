package com.conexiam.expirable.beans;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.service.cmr.repository.*;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpiredContentReportWriter implements ReportWriter {
    public static QName NAME = QName.createQName(NamespaceService.ALFRESCO_URI, "expired-content-report-writer");

    private static final Logger LOG = LoggerFactory.getLogger(ExpiredContentReportWriter.class);

    private SearchService searchService;
    private NodeService nodeService;
    private ContentService contentService;

    private String reportFolderPath;
    private String reportName = "expired-content-report";

    @Override
    public NodeRef save(List<ReportData> reportDataList) {
        String report = getReport(reportDataList);
        NodeRef reportFolder = getReportFolderNode(reportFolderPath);
        if (reportFolder == null) {
            LOG.error("Could not find the report folder: " + reportFolderPath);
            return null;
        }
        NodeRef reportNode = createContentNode(reportFolder, getReportFileName(), report);
        LOG.info("Expired content report writer saved a new report.");
        if (LOG.isDebugEnabled()) LOG.debug("Report node created: " + reportNode.toString());
        return reportNode;
    }

    @Override
    public String format(ReportData reportData) {
        return "nodeRef: " + reportData.getNodeRef() +
                " expired on: " + reportData.getExpirationDate() +
                " path: " + reportData.getPath() +
                " name: " + reportData.getName() + "\r\n";
    }

    public String getReportFileName() {
        return reportName + "-" + (new Date()).getTime() + ".txt";
    }

    private String getReport(List<ReportData> reportDataList) {
        StringBuffer sb = new StringBuffer();
        for (ReportData reportData : reportDataList) {
            sb.append(format(reportData));
        }
        return sb.toString();
    }

    private NodeRef getReportFolderNode(String folderPath) {
        String query = "PATH:\"" + folderPath + "\"";
        ResultSet results = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_FTS_ALFRESCO, query);
        if (results.length() <= 0) {
            return null;
        }
        return results.getNodeRef(0);
    }

    private NodeRef createContentNode(NodeRef parent, String name, String text) {

        Map<QName, Serializable> props = new HashMap<QName, Serializable>(1);
        props.put(ContentModel.PROP_NAME, name);

        // use the node service to create a new node
        NodeRef node = this.nodeService.createNode(
                parent,
                ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI, name),
                ContentModel.TYPE_CONTENT,
                props).getChildRef();

        // Use the content service to set the content onto the newly created node
        ContentWriter writer = this.contentService.getWriter(node, ContentModel.PROP_CONTENT, true);
        writer.setMimetype(MimetypeMap.MIMETYPE_TEXT_PLAIN);
        writer.setEncoding("UTF-8");
        writer.putContent(text);

        // Return a node reference to the newly created node
        return node;
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setContentService(ContentService contentService) {
        this.contentService = contentService;
    }

    public void setReportFolderPath(String reportFolderPath) {
        this.reportFolderPath = reportFolderPath;
    }

    @Override
    public void setName(String reportName) {
        this.reportName = reportName;
    }
}
