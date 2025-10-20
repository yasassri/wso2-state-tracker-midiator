package com.ycr.wso2.mediator.statetracker;

public enum StatusManagerType {
    /**
     * In-memory storage
     */
    IN_MEMORY("InMemory"),
    
    // Not implemented yet
    DATABASE("Database"),
    
    // Not implemented yet
    REGISTRY("Registry");
    
    private final String typeName;
    
    StatusManagerType(String typeName) {
        this.typeName = typeName;
    }
    
    public String getTypeName() {
        return typeName;
    }
    
    public static StatusManagerType fromString(String type) {
        if (type == null || type.trim().isEmpty()) {
            return IN_MEMORY; // Default
        }
        
        for (StatusManagerType managerType : StatusManagerType.values()) {
            if (managerType.name().equalsIgnoreCase(type.replace("-", "_").replace(" ", "_"))) {
                return managerType;
            }
        }
        
        throw new IllegalArgumentException("Unknown StatusManagerType: " + type + 
            ". Supported types: IN_MEMORY, DATABASE, REGISTRY");
    }
    
    @Override
    public String toString() {
        return typeName;
    }
}
