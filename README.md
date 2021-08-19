Coverage: 98.9% on src/main/java
![Alt](readme-img/1.png)

# To-Do List (TDL)

This is a CRUD web application with a Spring Boot backend and a Bootstrap-based frontend. The database comprises of two tables called Tasks and Assignees, as well as an intermediary table named Tasks_Assignees, created to handle the many-to-many relationship between Tasks and Assignees.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

[JDK 11 or over](https://www.oracle.com/java/technologies/javase-jre8-downloads.html)

[Maven](http://maven.apache.org/download.cgi)

[Spring Boot](https://spring.io/quickstart)

[Bootstrap 5](https://getbootstrap.com/)

[Git](https://git-scm.com/downloads)

#### Dependencies (already added to the pom.xml):

Spring Data JPA

Validation

H2 Database

Spring Web

Spring Fox

[Model Mapper](http://modelmapper.org/getting-started/)

[Selenium](https://robotframework.org/SeleniumLibrary/)

### Installing

To create a copy of this repository in to your local machine:

1. Fork this repository to your account

![](readme-img/2.png)

2. Using Git Bash, clone it to your local machine to a directory of your choosing

![](readme-img/3.png)

3. Open the repo using your chosen IDE

5. The project is now ready for development and testing!

## Running the tests

Three types of tests were deployed in regards to the validation of this program - unit, integration and user acceptance. 

Unit testing isolates individual units of the software and validates whether each of them performs as designed. These units are considered to be the smallest testable parts of an application. JUnit and Mockito were used to write these tests. Classes which contains unit tests have the same name as the class they are testing, followed by `UnitTest`.

Integration testing validates the functionality, performance and reliability of a section of units which interact with each other. It looks to expose defects in those interactions and verify that these software modules work in harmony with each other. The integration tests that come with this repository take a 'top down' approach; the classes which contain these tests have the name of the 'top' class, followed by `IntegrationTest`.

User acceptance testing validates the program against previously agreed requirements, as well as sees if it can be used by end-users. They are often designed as to mimic real-world use of the application. These tests can be found in the `UserAcceptanceTest` class.

To deploy tests using Maven, simply run `mvn clean test` in the root directory of your local repository.

## Deployment

To create a self-contained executable WAR using Maven:

1. Make sure the following `<packaging>` element is in your `pom.xml` file:

![](readme-img/6.png)

2. Open the directory containing the repository in a terminal
3. Run `mvn clean package`

![](readme-img/4.png)

3. If all tests pass and no hiccups occur, the WAR file should be located in the target folder.

![](readme-img/5.png)

## Built With

* [Java 11](https://www.oracle.com/java/)
* [Maven](https://maven.apache.org/) - Dependency Management
* [Spring Boot](https://spring.io/) - IOCC Framework
* [Bootstrap 5](https://getbootstrap.com/) - Frontend Framework
* [Git](https://git-scm.com/) - Version Control System
* [JUnit](https://junit.org/junit4/), [Mockito](https://site.mockito.org/) & [Selenium](https://robotframework.org/SeleniumLibrary/) - Testing
* [Sonarqube](https://www.sonarqube.org/) - Code Quality Inspection
* [Jira](https://www.atlassian.com/software/jira) - Project Management

## Authors

* **Siddhartha Gurung** (qasidd)

## License

This project is licensed under the MIT license - see the [LICENSE.md](LICENSE.md) file for details 

## Acknowledgments

* Team Trap
* Alan
* Savannah
* Vinesh
* Aswene
* Nick
