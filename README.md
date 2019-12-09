# Prerequisite
Java 8 or higher.
Maven 3.3 or higher.
# Installation
From the root of the project run :
mvn clean install.
# Start
From the root of the project :
mvn exec:java -Dexec.args="relativePathToOriginalPage relativePathToPageTObeCompared"
Ex: mvn exec:java -Dexec.args="samples/sample-0-origin.html samples/sample-1-evil-gemini.html"



