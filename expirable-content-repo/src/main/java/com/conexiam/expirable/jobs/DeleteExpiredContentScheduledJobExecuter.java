package com.conexiam.expirable.jobs;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteExpiredContentScheduledJobExecuter {
    private static final Logger LOG = LoggerFactory.getLogger(DeleteExpiredContentScheduledJobExecuter.class);

    /**
     * Public API access
     */
    private ServiceRegistry serviceRegistry;

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    /**
     * Executer implementation
     */
    public void execute() {
        LOG.info("Running the scheduled job: Delete Expired Content");

        ActionService actionService = serviceRegistry.getActionService();
        Action action = actionService.createAction("delete-expired-content");
        actionService.executeAction(action, null);
    }
}
