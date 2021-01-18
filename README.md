### Test automation project for a comprehensive service interacting with the Bank's DBMS and API

### Mysql build
[![Build status](https://ci.appveyor.com/api/projects/status/umyyr8ehkdoiao48?svg=true)](https://ci.appveyor.com/project/root1604/qa-diploma)

### Postgresql build
[![Build status](https://ci.appveyor.com/api/projects/status/sx7xdf20wqce2xt0?svg=true)](https://ci.appveyor.com/project/root1604/qa-diploma-5i6lg)

The task is to automate scenarios (both positive and negative) for buying a tour. 

Work order:  
1. [Test Automation Planning](documentation/Plan.md)
2. Setting up a test environment
3. Writing and debugging autotests
4. Test run and bug description
5. [Automation report](documentation/Summary.md)
6. [Test report](documentation/Report.md)
  
Launching a test environment in Linux OS (when working on the project, the Centos 7 distribution was used)
1. Check that the docker service is running and the current user has permission to execute the sudo command to work with docker (used in scripts for starting the test environment)
2. To start the environment using the Mysql database, run the following command (in order to avoid conflicts and to start the environment correctly, when the script starts, running containers and images saved in the system are first deleted)
```
sh startMysqlEnv.sh
```
3. To start the environment using Postgres, run the following command (to avoid conflicts and to start the environment correctly, running the script first removes running containers and images saved on the system)
```
sh startPostgresEnv.sh
```
4. Running tests.
```
./gradlew clean test
```
5. Allure report generation.
```
./gradlew allureReport
```
6. View allure report.
```
./gradlew allureServe
```
7. To remove all containers and docker images after testing run the following command
```
sh removeAllContainers.sh
```
