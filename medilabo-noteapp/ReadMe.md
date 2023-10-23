# **ReadMe**


## - **Note Application**

API that provides operations to interact with a MongoDB database containing notes. </br>
Spring Boot project that uses Java to run.


## - **Getting Started**

These instructions will get you a copy of the project up and running on your local machine.


## - **Prerequisites**

You need to install :
* Java 17
* Mavent 4.0.0
* MongoDB


## - **Installing**

* [Install Java](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
* [Install Maven](https://maven.apache.org/install.html)
* [Install MongoDB](https://www.mongodb.com/docs/manual/installation/)


### Reference Documentation

For further reference, please consider the following sections:
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#web)
* [Spring Data MongoDB](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)
* [Validation](https://docs.spring.io/spring-boot/docs/3.1.3/reference/htmlsingle/index.html#io.validation)

### Guides

The following guides illustrate how to use some features concretely:
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb//)
* [Validation](https://spring.io/guides/gs/validating-form-input/)


## - **Running App**

- Firstly, at the root of the project, you have to run the command ***"mvn clean install"***.

- You can access the app in a web broser : http://localhost:8083/

- By default, the app is in "production mode" : there are no data in database. </br>
	- You also have a "dev mode" in which you already have data. </br>
	- To switch the "dev mode" on, you have to, 
	in the folder **../gitMedilabo/medilabo-noteapp/src/main/java/com/medilabo/medilabonoteapp**, 
	open the file *MedilaboNoteappApplication.java* and replace : </br>
        - *"System.setProperty("spring.profiles.active","prod");"* </br>
        - by : *"System.setProperty("spring.profiles.active","dev");"* </br>
	- You have to run the command ***"mvn clean install"*** again </br>


## - **Tests**

The app has unit tests and integration tests written. </br>

After the ***"mvn clean install"*** command, you can get both Surefire and Jacoco Report 
in the folder **../target**. </br>

For Jacoco report, open the *index.html* in the folder **../jacoco**. </br>
For Surefire report, open the files in the folder **../surefire-reports**. </br>

