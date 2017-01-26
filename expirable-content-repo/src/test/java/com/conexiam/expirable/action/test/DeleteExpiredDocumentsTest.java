package com.conexiam.expirable.action.test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.*;

import com.conexiam.expirable.beans.ExpiredContentReportWriter;
import com.conexiam.expirable.beans.ReportData;
import com.conexiam.expirable.beans.ReportWriter;
import com.conexiam.expirable.model.ExpirableContentModel;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ActionService;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tradeshift.test.remote.Remote;
import com.tradeshift.test.remote.RemoteTestRunner;

@RunWith(RemoteTestRunner.class)
@Remote(runnerClass=SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:alfresco/application-context.xml")
public class DeleteExpiredDocumentsTest {

    private static final String ADMIN_USER_NAME = "admin";

    static Logger logger = Logger.getLogger(DeleteExpiredDocumentsTest.class);

    @Autowired
    @Qualifier("NodeService")
    protected NodeService nodeService;

    @Autowired
    @Qualifier("SearchService")
    protected SearchService searchService;

    @Autowired
    @Qualifier("ActionService")
    protected ActionService actionService;

    @Autowired
    @Qualifier("expired-content-report-writer")
    protected ReportWriter reportWriter;

    @Test
    public void testDeleteDocs() {
        AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);

        String query = "+PATH:\"/app:company_home/app:dictionary/app:expirable_content_reports\"";
        ResultSet rs = searchService.query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_FTS_ALFRESCO, query);

        NodeRef reportsFolder = rs.getNodeRef(0);


        String testFolderName = "testFolder-" + (new Date()).getTime();
        Map<QName, Serializable> contentProps = new HashMap<QName, Serializable>();
        contentProps.put(ContentModel.PROP_NAME, testFolderName);
        NodeRef testFolder = nodeService.createNode(reportsFolder,
                ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_PREFIX, testFolderName),
                ContentModel.TYPE_FOLDER,
                contentProps).getChildRef();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();

        createTestDoc(testFolder, "testDoc1", yesterday);
        createTestDoc(testFolder, "testDoc2", yesterday);
        createTestDoc(testFolder, "testDoc3", null); // does not expire

        List<ChildAssociationRef> children = nodeService.getChildAssocs(testFolder);
        assertEquals(3, children.size());

        Action deleteExpiredAction = actionService.createAction("delete-expired-content");
        actionService.executeAction(deleteExpiredAction, null);

        children = nodeService.getChildAssocs(testFolder);
        assertEquals(1, children.size());

        nodeService.deleteNode(testFolder);
    }

    @Test
    public void reportWriterTest() {
        ReportData reportData = new ReportData("workspace://SpacesStore/test123", "test-name", "/test/path", new Date());
        List<ReportData> reportDataList = new ArrayList<ReportData>();
        reportDataList.add(reportData);

        reportWriter.setName("test-report");
        NodeRef report = reportWriter.save(reportDataList);
        assertNotNull(report);
        assertTrue(nodeService.exists(report));

        nodeService.deleteNode(report);
    }

    private void createTestDoc(NodeRef testFolder, String testDocName, Date expirationDate) {
        Map<QName, Serializable> contentProps = new HashMap<QName, Serializable>();
        contentProps.put(ContentModel.PROP_NAME, testDocName);

        NodeRef testDoc = nodeService.createNode(testFolder,
                ContentModel.ASSOC_CONTAINS,
                QName.createQName(NamespaceService.CONTENT_MODEL_PREFIX, testDocName),
                ContentModel.TYPE_CONTENT,
                contentProps).getChildRef();

        if (expirationDate != null) {
            contentProps = new HashMap<QName, Serializable>();
            contentProps.put(ExpirableContentModel.PROP_EXPIRATION_DATE, expirationDate);
            nodeService.addAspect(testDoc, ExpirableContentModel.ASPECT_EXPIRABLE, contentProps);
        }
    }
}
