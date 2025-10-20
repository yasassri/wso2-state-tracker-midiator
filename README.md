# WSO2 State Tracker Mediator

A thread-safe WSO2 mediator for tracking process status with timestamp and expiry support.


## Building

```bash
mvn clean install
```

## Usage

### START_PROCESS
```xml
<property name="STATE_TRACKER_OPERATION" value="START_PROCESS"/>
<property name="PROCESS_IDENTIFIER" value="order-12345"/>
<property name="PROCESS_STATE_EXPIRY_TIME" value="3600"/>  <!-- optional -->
<class name="com.ycr.wso2.mediator.statetracker.StateTrackerMediator"/>
```

### IS_PROCESS_RUNNING
```xml
<property name="STATE_TRACKER_OPERATION" value="IS_PROCESS_RUNNING"/>
<property name="PROCESS_IDENTIFIER" value="order-12345"/>
<class name="com.ycr.wso2.mediator.statetracker.StateTrackerMediator"/>
<!-- Result in: PROCESS_IS_RUNNING (true/false) -->
```

### STOP_PROCESS
```xml
<property name="STATE_TRACKER_OPERATION" value="STOP_PROCESS"/>
<property name="PROCESS_IDENTIFIER" value="order-12345"/>
<class name="com.ycr.wso2.mediator.statetracker.StateTrackerMediator"/>
```

## Output Properties

- `STATE_TRACKER_RESULT` - Operation result
- `PROCESS_IS_RUNNING` - Boolean indicating if process is running
- `PROCESS_START_TIMESTAMP` - When the process started (milliseconds)
