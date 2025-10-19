# WSO2 MI State Tracker Mediator

Allows you to track the status of a process through WSO2 Micro Integrator (MI).

## Overview

The State Tracker Mediator is a custom class mediator for WSO2 MI that enables you to track the state of a process flow. It stores state information in message context properties, making it easy to monitor and debug complex integration flows.

## Features

- Track multiple states within a single message flow
- Store state information in a centralized map
- Access individual states via dedicated properties
- Configurable property names for state storage

## Building the Project

```bash
mvn clean install
```

This will create a JAR file in the `target/` directory.

## Installation

1. Build the project using Maven
2. Copy the generated JAR file (`org.wso2.carbon.mediator.statetracker-1.0.0.jar`) to `<MI_HOME>/lib` directory
3. Restart the WSO2 MI server

## Usage

### XML Configuration

Add the mediator to your sequence using the class mediator syntax:

```xml
<class name="org.wso2.carbon.mediator.statetracker.StateTrackerMediator">
    <property name="stateName" value="processStep"/>
    <property name="stateValue" value="started"/>
</class>
```

### Optional Configuration

You can customize the property name used to store states:

```xml
<class name="org.wso2.carbon.mediator.statetracker.StateTrackerMediator">
    <property name="stateName" value="processStep"/>
    <property name="stateValue" value="completed"/>
    <property name="stateProperty" value="CUSTOM_STATE_TRACKER"/>
</class>
```

### Example Sequence

```xml
<sequence name="SampleSequence">
    <!-- Track initial state -->
    <class name="org.wso2.carbon.mediator.statetracker.StateTrackerMediator">
        <property name="stateName" value="step1"/>
        <property name="stateValue" value="started"/>
    </class>
    
    <!-- Your business logic here -->
    <log level="custom">
        <property name="message" value="Processing step 1"/>
    </log>
    
    <!-- Track completion state -->
    <class name="org.wso2.carbon.mediator.statetracker.StateTrackerMediator">
        <property name="stateName" value="step1"/>
        <property name="stateValue" value="completed"/>
    </class>
</sequence>
```

## Accessing Tracked States

Tracked states are stored in two ways:

1. **In a Map**: All states are stored in a map under the property `STATE_TRACKER` (or your custom property name)
2. **Individual Properties**: Each state is also accessible via `STATE_TRACKER_{stateName}`

## Configuration Properties

| Property | Required | Default | Description |
|----------|----------|---------|-------------|
| stateName | Yes | - | The name of the state to track |
| stateValue | Yes | - | The value to assign to the state |
| stateProperty | No | STATE_TRACKER | The property name used to store the state map |

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.
