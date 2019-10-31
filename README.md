#Cucumber Selenium Spring Boot Test Automation Suite

##Prerequisite
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

##How to setup
1. Clone the repository to your local setup.  
  You can use the following sample command to do so:
```
git clone https://gitlab.cpfb.gds-gov.tech/cdit/cdit-automation.git
```
2. Use maven to install all project dependencies
3. Install IDE's cucumber plugin

##How to run
1. Goto any file with a .feature file type
2. Right click on any scenario and click 'Run scenario'
3. If test passes, you are good to go.

##File structure
* All BDD code are stored under src/test/java/resources/cdit_automation/*.feature  
  Cucumber can only run test which are in the *.feature file
  
* All of cucumber scenarios are made up of steps. All steps have their corresponding step definitions.  
  All step definition files are stored under /src/test/java/cdit_automation/step_definition/ directory
  
## Notable classes and their purposes

##How to write a BDD test scenario