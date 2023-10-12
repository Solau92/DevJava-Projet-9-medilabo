# **ReadMe**

## - **MediLabo Solutions Application**

An app intented to detect the level of diabetes risk of patients. </br>


## - **Getting Started**

These instructions will get you a copy of the project up and running on your local machine.


## - **Installing**

You need to install Docker ...........

Then you have to download the whole project in your machine.

At the root of the project, you have a file named *docker-compose.yml*.

You must complete the following fiels : 
- in medilabo-patient : 
	SPRING_DATASOURCE_USERNAME: *enter the username of your local database*
    SPRING_DATASOURCE_PASSWORD: *enter the password of your local database*
- in mysqldb : 
	MYSQL_ROOT_PASSWORD: *enter the root password of your local database*
    MYSQL_PASSWORD: *enter the username of your local database*
- in medilabo-note : 
	....


### Reference Documentation

For further reference, please consider the following sections:



## - **Running App**

- mvn clean install ??

- root directory of the project (were docker-compose.yml)
- docker-compose up 

- in a web broser : http://localhost:18080/

- credentials : 
	. username : medilaboUser, password : medilaboUserPassword
	. admin : medilaboAdmin, password : medilaboAdminPassword



## - **Tests**

- test