package com.ycr.wso2.mediator.statetracker;

/**
 * Represents a process state entry with timestamp and expiry
 */
public class ProcessStateEntry {
    private final String projectId;
    private final long startTimestamp;
    private final long expiryTimeMillis;
    
    public ProcessStateEntry(String projectId, long startTimestamp, long expiryTimeMillis) {
        this.projectId = projectId;
        this.startTimestamp = startTimestamp;
        this.expiryTimeMillis = expiryTimeMillis;
    }
    
    public String getProjectId() {
        return projectId;
    }
    
    public long getStartTimestamp() {
        return startTimestamp;
    }
    
    public long getExpiryTimeMillis() {
        return expiryTimeMillis;
    }
    
    public boolean isExpired() {
        if (expiryTimeMillis <= 0) {
            return false; // No expiry set
        }
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTimestamp) > expiryTimeMillis;
    }
    
    public boolean isRunning() {
        return !isExpired();
    }
    
    @Override
    public String toString() {
        return "ProcessStateEntry{" +
                "projectId='" + projectId + '\'' +
                ", startTimestamp=" + startTimestamp +
                ", expiryTimeMillis=" + expiryTimeMillis +
                ", isRunning=" + isRunning() +
                '}';
    }
}
