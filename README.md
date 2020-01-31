#Cucumber Selenium Spring Boot Test Automation Suite
---
##Table of contents
- ####[Prerequisites](##rerequisite)  
- ####[Setting up](##how-to-setup)  
- ####[File structure](#file-structure)
    - ####[Notable File structure](#notable-file-structure)  
    - ####[Notable classes and their purposes](#notable-classes-and-their-purposes)
- ####[How it works](#how-it-works)
- ####[Running the test suite](#running-the-test-suite)
    - ####[Running a scenario using Intellij](#running-a-scenario-using-intellij)  
    - ####[Running via Command Line](#running-via-command-line)  
    - ####[Running via Command Line with tags](#running-via-command-line-with-tags)
    - ####[Running via an executable jar](#running-via-an-executable-jar)
        - ####[Preparing the jar](#preparing-the-jar)
    - ####[Running via an executable jar with tags](#running-via-an-executable-jar-with-tags)
        - ####[Preparing the jar](#preparing-the-jar)
    - ####[Running via bash script](#running-via-bash-script)
- ####[Restrictions](restrictions)

---
## Prerequisites
1. Download chromedriver (https://chromedriver.chromium.org/downloads)
2. For mac/linux setup, you can place the file in any location in your harddrive and symlink it to /usr/local/bin  
   You can use the following command to perform the symlink
```
ln -s <absolute_path_to_source_file> /usr/local/bin/chromedriver 
```
3. Java IDE i.e. Intellij, Ecslipse
4. Maven plugin
5. Java Runtime(JRE) and Java Development Kit(JDK)
6. Git
---
##Setting up
1. Clone the repository to your local setup.  
  You can use the following sample command to do so:
```
git clone https://gitlab.cpfb.gds-gov.tech/cdit/cdit-automation.git
```
2. Use maven to install all project dependencies
3. Install IDE's cucumber plugin
---
## File Structure
### Notable File structure
#####Cucumber Feature Files
* All BDD code are stored under src/main/java/resources/cdit_automation/*.feature  
  Cucumber can only run test which are in the *.feature file
#####Cucumber Step Definitions
* All of cucumber scenarios are made up of steps. All steps have their corresponding step definitions.  
  All step definition files must be stored under /src/main/java/cdit_automation/step_definition/* directory  

  It would be highly encouraged to name your step file ending with 'Steps'. The only Step file that does  
  not follow this naming convention is the Hooks file. This naming convention is purely to increase readability.  
  If you create a new Step file, you need to extend the AbstractStep class because much of the test suites' functions are autowired in there.
#####Database Querying Functionalities
* All JpaRepositories are housed under src/main/java/cdit_automation/respositories directory
#####Models
* All models or entities are housed under src/main/java/cdit_automation/models directory
#####Fake Data Generation
* All general fake data are currently housed within the Phaker class. i.e. creating a random phone number, NRIC, random valid date.
    * Note that Phaker only creates field level fake data. It's not meant to create multiple database records i.e. creating a person in database.
#####Data generation
* All model data generation is housed under src/main/java/cdit_automation/data_helpers directory
#####Firing API calls
* All APIs are housed in the ApiHelper class under src/main/java/cdit_automation/api_helpers directory
#####Asserts
* All test assertations are housed in the Assert class under src/main/java/cdit_automation/asserts directory  
---
###Notable classes and their purposes
* TestManager
    * This class keeps track of all information regarding the test run. This class is instantiated ONLY once and lives throughout the test run.
* PageUtils
    * This class is purely used to house all UI navigational functions . i.e. clicking on an button, link on the browser.
* DriverManager
    * This class is used to control browser driver functionality. i.e. creating or destroying a browser driver.
* ApiHelper
    * This class is used to create and fire Api calls to any system under test.
* TestEnvEnums
    * This enum class is used to house all test environment related information. i.e. database connections, urls, ports.
---
##How it works
* Entry point to the test suite
    * RunCucumberTest is the entry point to the test suite.
* How does spring boot come into the picture?
    * Cucumber-spring is the dependency that is required to create the application context.
    * This context is shared across all test scenarios during the automation run.
    * In order for Cucumber to be aware of the test configurations, you simply need a class to be annotated with @SpringBootTest. In this case, we use the SpringContextLoader class.
##Running the test suite
###Running a scenario using Intellij
1. Goto any file with a .feature file type
2. Right click on any scenario and click 'Run scenario'
3. If test passes, you are good to go.
---
###Running via Command line
1. Take note of the feature file which your test scenario resides in and the line number.  
   I.e. mha_dual_citizen.feature:7
2. In your terminal, go to the cdit-automation project root directory.
3. Type the following
```
mvn test -Dcucumber.options="src/main/resources/cdit_automation/test.feature:20"
```
---
###Running via Command line with tags
Run with just 1 tag
```
mvn test -Dcucumber.options="src/main/resources/cdit_automation/test.feature:20 --tags @tagname"
```
Run with multiple tags with AND condition
```
mvn test -Dcucumber.options="src/main/resources/cdit_automation/test.feature:20 --tags @tagname1 --tags @tagname2"
```
Run with multiple tags with OR condition (Take note: No spacing between tag names)
```
mvn test -Dcucumber.options="src/main/resources/cdit_automation/test.feature:20 --tags @tagname1,@tagname2"
```
Run with exclude tags
```
mvn test -Dcucumber.options="src/main/resources/cdit_automation/test.feature:20 --tags @tagname1 --tags ~@tagname2"
```
Running in

---
###Running via an executable jar
####Preparing the jar  
1. The jars are located in the subdirectory deployment/
2. If it does not exists, run the following command:
```
mvn clean package -DskipTests=true
```
Assuming your current working directory is the cdit-automation project root
```
java -cp "${$(pwd)}/deployment/uber-cdit-automation.jar:${$(pwd)}/deployment/lib/*" cdit_automation.RunCucumberTest
```
###Running via an executable jar with tags
Run with just 1 tag
```
java -cp "${$(pwd)}/deployment/uber-cdit-automation.jar:${$(pwd)}/deployment/lib/*" cdit_automation.RunCucumberTest --tags @tagname
```
Run with multiple tags with AND condition
```
java -cp "${$(pwd)}/deployment/uber-cdit-automation.jar:${$(pwd)}/deployment/lib/*" cdit_automation.RunCucumberTest --tags @tagname1 --tags ~@tagname2
```
Run with multiple tags with OR condition
```
java -cp "${$(pwd)}/deployment/uber-cdit-automation.jar:${$(pwd)}/deployment/lib/*" cdit_automation.RunCucumberTest --tags @tagname1,@tagname2
```
Run with exclude tags
```
java -cp "${$(pwd)}/deployment/uber-cdit-automation.jar:${$(pwd)}/deployment/lib/*" cdit_automation.RunCucumberTest --tags ~@tagname
```
---
###Running via bash script
The run.sh script is stored under the subdirectory scripts.  
Assuming your current working directory is the cdit-automation project root
```
bash script/run.sh
```

###Steps to resolve failure to find OJDBC8 12.2.0.1 library
1.   Copy OJDBC 12.2.0.1.jar to project /resources and rename as ojdbc.jar
```
cp ~/.m2/repository/com/oracle/ojdbc8/12.2.0.1/ojdbc8-12.2.0.1.jar ~/folder/cdit-automation/src/main/resources/ojdbc.jar
```
2. Run the below command to install the jar to the project
```
mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc8 \ -Dversion=12.2.0.1 -Dpackaging=jar -Dfile=./src/main/resources/ojdbc.jar
```

###Restrictions
1. If you are using JetBrains's Intellij to run this test suite, you must use the community edition. The cucumber and gherkin plugins only work on the community version.
2. Intellij Community Edition 2019.3.1's "Cucumber for Java" plugin version 193.5662.7 does not support Cucumber's 5.1.0 @ParameterType feature so it'll look like there's a undefined step but it works actually.