package com.ycr.wso2.mediator.statetracker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Factory for creating ProcessStatusManager instances
 */
public class ProcessStatusManagerFactory {
    
    private static final Log log = LogFactory.getLog(ProcessStatusManagerFactory.class);
    
    // Singleton instances for each manager type
    private static volatile ProcessStatusManager inMemoryInstance;
    private static volatile ProcessStatusManager databaseInstance;
    private static volatile ProcessStatusManager registryInstance;
    
    private ProcessStatusManagerFactory() {
    }
    
    /**
     * Get ProcessStatusManager instance based on type
     */
    public static ProcessStatusManager getManager(StatusManagerType type) {
        if (type == null) {
            type = StatusManagerType.IN_MEMORY;
        }
        
        switch (type) {
            case IN_MEMORY:
                return getInMemoryInstance();
                
            case DATABASE:
                return getDatabaseInstance();
                
            case REGISTRY:
                return getRegistryInstance();
                
            default:
                throw new IllegalArgumentException("Unknown StatusManagerType: " + type);
        }
    }
    
    private static ProcessStatusManager getInMemoryInstance() {
        if (inMemoryInstance == null) {
            synchronized (ProcessStatusManagerFactory.class) {
                if (inMemoryInstance == null) {
                    inMemoryInstance = new InMemoryProcessStatusManager();
                    if (log.isDebugEnabled()) {
                        log.debug("Created new InMemoryProcessStatusManager instance");
                    }
                }
            }
        }
        return inMemoryInstance;
    }
    
    private static ProcessStatusManager getDatabaseInstance() {
        throw new UnsupportedOperationException(
            "DatabaseProcessStatusManager is not yet implemented. " +
            "Please use IN_MEMORY type for now."
        );
    }
    
    private static ProcessStatusManager getRegistryInstance() {
        throw new UnsupportedOperationException(
            "RegistryProcessStatusManager is not yet implemented. " +
            "Please use IN_MEMORY type for now."
        );
    }
}
