# Build Instructions for WSO2 MI State Tracker Mediator

## Prerequisites

To build this WSO2 MI class mediator project, you need:

1. **Java Development Kit (JDK) 8 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Set JAVA_HOME environment variable

2. **Apache Maven 3.6+**
   - Download from: https://maven.apache.org/download.cgi
   - Add Maven bin directory to your PATH

3. **WSO2 Micro Integrator (MI)**
   - Download from: https://wso2.com/integration/micro-integrator/
   - This provides the runtime environment for the mediator

4. **Access to WSO2 Maven Repository**
   - The build requires access to https://maven.wso2.org/nexus/content/groups/wso2-public/
   - Ensure your network allows access to this repository

## Building the Project

### Option 1: Build with WSO2 Dependencies (Requires Repository Access)

If you have access to WSO2's Maven repository, add the following dependencies to the pom.xml:

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.synapse</groupId>
        <artifactId>synapse-core</artifactId>
        <version>4.0.0-wso2v109</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.apache.synapse</groupId>
        <artifactId>synapse-commons</artifactId>
        <version>4.0.0-wso2v109</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>commons-logging</groupId>
        <artifactId>commons-logging</artifactId>
        <version>1.1.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

Then run:

```bash
mvn clean install
```

### Option 2: Build in WSO2 Integration Studio

1. Download and install WSO2 Integration Studio
2. Import this project as a Maven project
3. The IDE will handle dependency resolution
4. Build using the IDE's build tools

### Option 3: Manual Compilation (For Development)

If you only need to verify the code structure without building:

```bash
# Just validate the project structure
mvn validate

# Package without running tests
mvn clean package -DskipTests
```

## Deployment

After successful build, you'll find the JAR file in `target/` directory:

```
target/org.wso2.carbon.mediator.statetracker-1.0.0.jar
```

### Deploy to WSO2 MI

1. Copy the JAR file to the WSO2 MI lib directory:
   ```bash
   cp target/org.wso2.carbon.mediator.statetracker-1.0.0.jar <MI_HOME>/lib/
   ```

2. Restart WSO2 MI:
   ```bash
   cd <MI_HOME>/bin
   ./micro-integrator.sh     # Linux/Mac
   micro-integrator.bat      # Windows
   ```

3. Use the mediator in your sequences (see README.md for usage examples)

## Troubleshooting

### Cannot Access WSO2 Repository

If you cannot access the WSO2 Maven repository:

1. Check your network/firewall settings
2. Use WSO2 Integration Studio which has offline dependency management
3. Contact WSO2 support for alternative repository access
4. Use a VPN if the repository is geo-restricted

### Compilation Errors

- Ensure you're using JDK 8 or higher
- Verify Maven is properly installed: `mvn --version`
- Clear Maven cache: `rm -rf ~/.m2/repository/org/apache/synapse`
- Try offline mode if you have cached dependencies: `mvn -o clean install`

### Runtime Errors

- Verify the JAR is in the correct lib directory
- Check MI logs: `<MI_HOME>/repository/logs/wso2carbon.log`
- Ensure WSO2 MI version is compatible (tested with MI 4.x)

## Project Structure

```
.
├── pom.xml                          # Maven project configuration
├── artifact.xml                     # WSO2 artifact descriptor
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/wso2/carbon/mediator/statetracker/
│   │   │       └── StateTrackerMediator.java
│   │   └── resources/
│   └── test/
│       └── java/
│           └── org/wso2/carbon/mediator/statetracker/
│               └── StateTrackerMediatorTest.java
├── README.md                        # Usage documentation
└── BUILD.md                         # This file
```

## Additional Resources

- [WSO2 MI Documentation](https://ei.docs.wso2.com/en/latest/micro-integrator/overview/introduction/)
- [Creating Custom Mediators](https://ei.docs.wso2.com/en/latest/micro-integrator/develop/customizations/creating-custom-mediators/)
- [WSO2 Integration Studio](https://wso2.com/integration/integration-studio/)
