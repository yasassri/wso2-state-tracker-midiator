package com.ycr.wso2.mediator.statetracker;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * State Tracker Mediator - Tracks process status 
 */
public class StateTrackerMediator extends AbstractMediator {

    private static final Log log = LogFactory.getLog(StateTrackerMediator.class);
    
    @Override
    public boolean mediate(MessageContext messageContext) {
        
        log.debug("State Tracker Mediator :: mediate()");
  
        try {
            // Read operation
            String operation = getStringProperty(messageContext, Contants.STATE_TRACKER_OPERATION_PROPERTY);
            if (operation == null || operation.trim().isEmpty()) {
                handleError("STATE_TRACKER_OPERATION property is required", null);
                messageContext.setProperty(Contants.STATE_TRACKER_RESULT_PROPERTY, "ERROR");
                messageContext.setProperty(Contants.STATE_TRACKER_ERROR_PROPERTY, "Operation not specified");
                return false;
            }
            
            // Read process ID
            String processId = getStringProperty(messageContext, Contants.PROCESS_IDENTIFIER_PROPERTY);
            if (processId == null || processId.trim().isEmpty()) {
                handleError("PROCESS_IDENTIFIER property is required", null);
                messageContext.setProperty("STATE_TRACKER_RESULT", "ERROR");
                messageContext.setProperty("STATE_TRACKER_ERROR", "Process ID not specified");
                return false;
            }
            
            synchronized (processId.intern()) {
                
                log.debug("Acquired lock for process: " + processId);
                
                ProcessStatusManager manager = getOrInitManager();
                boolean result = performOperation(messageContext, manager, processId, operation);
                
                log.debug("Operation " + operation + " completed for process: " + processId);

                return result;
            }
            
        } catch (Exception e) {
            handleError("Error in State Tracker Mediator", e);
            messageContext.setProperty("STATE_TRACKER_RESULT", "ERROR");
            messageContext.setProperty("STATE_TRACKER_ERROR", e.getMessage());
            return false;
        }
    }

    /**
     * Get or initialize ProcessStatusManager based on TYPE
     */
    private ProcessStatusManager getOrInitManager() {

        // Initial impl only supports IN_MEMORY so harcoding the type
        String managerTypeStr = "IN_MEMORY";

        // Get manager
        StatusManagerType managerType = StatusManagerType.fromString(managerTypeStr);
        ProcessStatusManager manager = ProcessStatusManagerFactory.getManager(managerType);
        
        if (log.isDebugEnabled()) {
            log.debug("Using ProcessStatusManager type: " + managerType);
        }
        
        return manager;
    }
    
    /**
     * Perform the requested operation
     */
    private boolean performOperation(MessageContext messageContext, ProcessStatusManager manager, 
                                     String processId, String operation) {
        
        switch (operation.toUpperCase()) {
            case "START_PROCESS":
                return performStartProcess(messageContext, manager, processId);
                
            case "IS_PROCESS_RUNNING":
                return performIsProcessRunning(messageContext, manager, processId);
                
            case "STOP_PROCESS":
                return performStopProcess(messageContext, manager, processId);

            default:
                handleError("Unsupported operation: " + operation, null);
                messageContext.setProperty("STATE_TRACKER_RESULT", "ERROR");
                messageContext.setProperty("STATE_TRACKER_ERROR", "Unsupported operation: " + operation);
                return false;
        }
    }
    
    private boolean performStartProcess(MessageContext messageContext, ProcessStatusManager manager, String processId) {
        try {

            long expiryTimeSeconds = getLongProperty(messageContext, Contants.STATE_EXPIRY_TIME_PROPERTY, 0);
            long expiryTimeMillis = expiryTimeSeconds > 0 ? expiryTimeSeconds * 1000 : 0;
            
            // Start the process
            manager.startProcess(processId, expiryTimeMillis);
            
            // Store result in message context
            messageContext.setProperty("STATE_TRACKER_RESULT", "STARTED");
            messageContext.setProperty("PROCESS_START_TIMESTAMP", System.currentTimeMillis());
            if (expiryTimeMillis > 0) {
                messageContext.setProperty("PROCESS_EXPIRY_TIME_MS", expiryTimeMillis);
            }

            log.debug("Started process: " + processId + " with expiry: " + expiryTimeSeconds + " seconds");

            return true;
            
        } catch (Exception e) {
            handleError("Error starting process: " + processId, e);
            messageContext.setProperty("STATE_TRACKER_RESULT", "ERROR");
            messageContext.setProperty("STATE_TRACKER_ERROR", e.getMessage());
            return false;
        }
    }

    private boolean performIsProcessRunning(MessageContext messageContext, ProcessStatusManager manager, String projectId) {
        try {
            boolean isRunning = manager.isProcessRunning(projectId);
            
            ProcessStateEntry entry = null;
            if (isRunning) {
                entry = manager.getProcessState(projectId);
            }
            
            // Store result in message context
            messageContext.setProperty("STATE_TRACKER_RESULT", isRunning ? "RUNNING" : "NOT_RUNNING");
            messageContext.setProperty("PROCESS_IS_RUNNING", isRunning);
            
            if (entry != null) {
                messageContext.setProperty("PROCESS_START_TIMESTAMP", entry.getStartTimestamp());
                messageContext.setProperty("PROCESS_EXPIRY_TIME_MS", entry.getExpiryTimeMillis());
                long elapsedTime = System.currentTimeMillis() - entry.getStartTimestamp();
                messageContext.setProperty("PROCESS_ELAPSED_TIME_MS", elapsedTime);
            }
            
            if (log.isDebugEnabled()) {
                log.debug("Process " + projectId + " is running: " + isRunning);
            }
            
            return true;
            
        } catch (Exception e) {
            handleError("Error checking process: " + projectId, e);
            messageContext.setProperty("STATE_TRACKER_RESULT", "ERROR");
            messageContext.setProperty("STATE_TRACKER_ERROR", e.getMessage());
            return false;
        }
    }

    private boolean performStopProcess(MessageContext messageContext, ProcessStatusManager manager, String projectId) {
        try {
            boolean stopped = manager.stopProcess(projectId);
            
            // Store result in message context
            messageContext.setProperty("STATE_TRACKER_RESULT", stopped ? "STOPPED" : "NOT_FOUND");
            messageContext.setProperty("PROCESS_STOPPED", stopped);
            
            if (log.isDebugEnabled()) {
                log.debug("Stop process " + projectId + ": " + (stopped ? "SUCCESS" : "NOT_FOUND"));
            }
            
            return true;
            
        } catch (Exception e) {
            handleError("Error stopping process: " + projectId, e);
            messageContext.setProperty("STATE_TRACKER_RESULT", "ERROR");
            messageContext.setProperty("STATE_TRACKER_ERROR", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get string property from message context
     */
    private String getStringProperty(MessageContext messageContext, String propertyName) {
        Object prop = messageContext.getProperty(propertyName);
        return (prop != null) ? prop.toString().trim() : null;
    }
    
    private long getLongProperty(MessageContext messageContext, String propertyName, long defaultValue) {
        Object prop = messageContext.getProperty(propertyName);
        if (prop == null) {
            return defaultValue;
        }
        
        try {
            return Long.parseLong(prop.toString().trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid long value for property " + propertyName + ": " + prop);
            return defaultValue;
        }
    }

    private void handleError(String message, Exception e) {
        if (e != null) {
            log.error(message, e);
        } else {
            log.error(message);
        }
    }
}
