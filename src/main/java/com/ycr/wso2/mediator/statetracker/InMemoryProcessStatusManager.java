package com.ycr.wso2.mediator.statetracker;

import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ProcessStatusManager
 * 
 * Thread-safety is handled at the mediator level, so this class
 * doesn't need internal locks. Uses ConcurrentHashMap for safe concurrent access.
 */
public class InMemoryProcessStatusManager implements ProcessStatusManager {
    
    private final ConcurrentHashMap<String, ProcessStateEntry> processMap;
    
    public InMemoryProcessStatusManager() {
        this.processMap = new ConcurrentHashMap<>();
    }
    
    @Override
    public void startProcess(String processId, long expiryTimeMillis) {
        if (processId == null || processId.trim().isEmpty()) {
            throw new IllegalArgumentException("Process ID cannot be null or empty");
        }
        
        long startTimestamp = System.currentTimeMillis();
        ProcessStateEntry entry = new ProcessStateEntry(processId, startTimestamp, expiryTimeMillis);
        processMap.put(processId, entry);
    }
    
    @Override
    public boolean isProcessRunning(String processId) {
        if (processId == null || processId.trim().isEmpty()) {
            return false;
        }
        
        ProcessStateEntry entry = processMap.get(processId);
        if (entry == null) {
            return false;
        }
        
        // Check if expired
        if (entry.isExpired()) {
            processMap.remove(processId);
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean stopProcess(String processId) {
        if (processId == null || processId.trim().isEmpty()) {
            return false;
        }
        
        return processMap.remove(processId) != null;
    }
    
    @Override
    public ProcessStateEntry getProcessState(String processId) {
        if (processId == null || processId.trim().isEmpty()) {
            return null;
        }
        
        ProcessStateEntry entry = processMap.get(processId);
        
        // Auto-cleanup if expired
        if (entry != null && entry.isExpired()) {
            processMap.remove(processId);
            return null;
        }
        
        return entry;
    }
    
}
