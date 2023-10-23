# **ReadMe**


## - **Front Application**

Application that provides a front-end for the Application MédiLabo Solutions. </br>
Spring Boot project that uses Java to run.


## - **Getting Started**

These instructions will get you a copy of the project up and running on your local machine.


## - **Prerequisites**

You need to install :
* Java 17
* Maven 4.0.0


## - **Installing**

* [Install Java](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
* [Install Maven](https://maven.apache.org/install.html)

### Reference Documentation

For further reference, please consider the following sections:
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.6/maven-plugin/reference/html/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#web)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#web.servlet.spring-mvc.template-engines)
* [Validation](https://docs.spring.io/spring-boot/docs/3.0.6/reference/htmlsingle/#io.validation)
* [Cloud Bootstrap](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/)

### Guides

The following guides illustrate how to use some features concretely:
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)


## - **Running App**

- Firstly, at the root of the project, you have to run the command ***"mvn clean install"***.

- You can access the app in a web broser : http://localhost:8080/


## - **Tests**

The app has unit tests and integration tests written. </br>

After the ***"mvn clean install"*** command, you can get both Surefire and Jacoco Report 
in the folder **../target**. </br>

For Jacoco report, open the *index.html* in the folder **../jacoco**. </br>
For Surefire report, open the files in the folder **../surefire-reports**. </br>

