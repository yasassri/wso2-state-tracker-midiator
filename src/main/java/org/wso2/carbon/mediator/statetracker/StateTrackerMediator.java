package org.wso2.carbon.mediator.statetracker;

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * State Tracker Mediator - Allows you to track the status of a process
 * 
 * This mediator can be used to track the state of a process flow through WSO2 MI.
 * It stores state information in message context properties.
 */
public class StateTrackerMediator extends AbstractMediator {

    private static final Log log = LogFactory.getLog(StateTrackerMediator.class);
    
    private String stateName;
    private String stateValue;
    private String stateProperty = "STATE_TRACKER";
    
    /**
     * Mediate method - the main entry point for the mediator
     * 
     * @param messageContext the current message context
     * @return true if mediation should continue, false otherwise
     */
    public boolean mediate(MessageContext messageContext) {
        
        if (log.isDebugEnabled()) {
            log.debug("State Tracker Mediator :: mediate()");
        }
        
        try {
            // Get or create the state map
            @SuppressWarnings("unchecked")
            Map<String, String> stateMap = (Map<String, String>) messageContext.getProperty(stateProperty);
            
            if (stateMap == null) {
                stateMap = new HashMap<>();
                messageContext.setProperty(stateProperty, stateMap);
            }
            
            // Track the state
            if (stateName != null && stateValue != null) {
                stateMap.put(stateName, stateValue);
                
                if (log.isDebugEnabled()) {
                    log.debug("State tracked: " + stateName + " = " + stateValue);
                }
            }
            
            // Also set individual property for easy access
            if (stateName != null && stateValue != null) {
                messageContext.setProperty(stateProperty + "_" + stateName, stateValue);
            }
            
        } catch (Exception e) {
            log.error("Error while tracking state", e);
            return false;
        }
        
        return true;
    }

    /**
     * Get the state name
     * 
     * @return the state name
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * Set the state name
     * 
     * @param stateName the state name to set
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     * Get the state value
     * 
     * @return the state value
     */
    public String getStateValue() {
        return stateValue;
    }

    /**
     * Set the state value
     * 
     * @param stateValue the state value to set
     */
    public void setStateValue(String stateValue) {
        this.stateValue = stateValue;
    }

    /**
     * Get the state property name
     * 
     * @return the state property name
     */
    public String getStateProperty() {
        return stateProperty;
    }

    /**
     * Set the state property name
     * 
     * @param stateProperty the state property name to set
     */
    public void setStateProperty(String stateProperty) {
        this.stateProperty = stateProperty;
    }
}
