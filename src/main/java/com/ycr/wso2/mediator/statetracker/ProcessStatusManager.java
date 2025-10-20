package com.ycr.wso2.mediator.statetracker;

/**
 * StatusManager interface for process tracking
 */
public interface ProcessStatusManager {
    
    /**
     * Start a process with optional expiry time
     * 
     * @param processId The unique identifier for the process
     * @param expiryTimeMillis Expiry time in milliseconds (0 or negative means no expiry)
     */
    void startProcess(String processId, long expiryTimeMillis);
    
    /**
     * Check if a process is running (started and not expired)
     * 
     * @param processId The unique identifier for the process
     * @return true if process is running, false otherwise
     */
    boolean isProcessRunning(String processId);
    
    /**
     * Stop a process (remove from tracking)
     * 
     * @param processId The unique identifier for the process
     * @return true if process was found and stopped, false otherwise
     */
    boolean stopProcess(String processId);
    
    /**
     * Get process state entry with full details
     * 
     * @param processId The unique identifier for the process
     * @return ProcessStateEntry or null if not found
     */
    ProcessStateEntry getProcessState(String processId);
    
}
