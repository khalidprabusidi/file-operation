# Log Analytics CLI

## Instructions

```sh
# First execute this command to build the project
mvn clean dependency:copy-dependencies package

# Run this command with arguments at -Dexec.args="...", E.g.
mvn compile exec:java -Dexec.mainClass="com.kps.loganalytic.App" -Dexec.args="analytics -t 2m -d /logdirectory/logs"
```