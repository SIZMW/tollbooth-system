Tollbooth Tollgate System
=========================

## Description
This project is used to demonstrate a tollbooth tollgate system built from specified user stories that describe how the tollbooth and the tollgate controllers interact.

## Building
This project can be imported into [Eclipse](https://eclipse.org/downloads/) as a project, which can then compile it.

## Execution
The project can be executed by running the [JUnit](http://junit.org/) test cases under the ```test``` directory.

#### Statistics
Coverage statistics can be generated using the [ECLEmma](http://eclemma.org/) plugin for Eclipse. Coverage was focused on the following classes:
* ```TollGate.java```
* ```TollboothLogger.java```
* ```SimpleLogger.java```
* ```LogMessage.java```
* ```TollboothException.java```

Auditing can be done with the [CodePro AnalytiX](https://marketplace.eclipse.org/content/codepro-analytix) plugin for Eclipse. Audits were run on all classes, removing any of the high or medium issues using a custom rule set.
