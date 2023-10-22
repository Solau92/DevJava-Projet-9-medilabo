# **ReadMe**

## - **MediLabo Solutions Application**

An app intended to detect the level of diabetes risk of patients. </br>


## - **Getting Started**

These instructions will get you a copy of the project up and running on your local machine.


## - **Installing**

You need to install Docker.

Then you have to download the whole project in your machine.

At the root of the whole project **../gitMedilabo**, you have a file named *docker-compose.yml*, you must complete the following fields in this file : 
  - in medilabo-patient : </br>
      - SPRING_DATASOURCE_USERNAME: *enter the username of your local database* </br>
      - SPRING_DATASOURCE_PASSWORD: *enter the password of your local database* </br>
  - in mysqldb : </br>
      - MYSQL_ROOT_PASSWORD: *enter the root password of your local database* </br>
      - MYSQL_PASSWORD: *enter the username of your local database* </br>
	
In the directory **../gitMedilabo/medilabo-patientapp/src/main/resources**, in these three files : </br>
- *application-dev.properties* </br>
- *application-prod.properties* </br>
- *application-test.properties* </br>
You must fill the following fields of your local MySQL database : </br>
- spring.datasource.username= </br>
- spring.datasource.password= </br>

### Reference Documentation

For further reference, please consider the following sections:

* [Docker](https://docs.docker.com/)
* [Install Docker Engine](https://docs.docker.com/engine/install/)

## - **Running App**

- Firstly, for each of the five services, you have to run the command ***"mvn clean install"*** at the root of the application.

- Then, at the root of the project **/gitMedilabo**, you have to run this command : 
***"docker compose up"***

- You can access the app in a web broser : http://localhost:18080/

- You can use these credentials : 
	- username : medilaboUser, password : medilaboUserPassword
	- admin : medilaboAdmin, password : medilaboAdminPassword

- By default, the app is in "production mode" : there are no data in database. </br>
	- You also have a "dev mode" in which you already have data. </br>
	- To switch the "dev mode" on, you have too : </br>
      - In the folder **../gitMedilabo/medilabo-patientapp/src/main/java/com/medilabo/medilabopatientapp**, open the file *MedilaboPatientappApplication.java* </br>
and replace : </br>
    	- *"System.setProperty("spring.profiles.active","prod");"* </br>
      	- by : *"System.setProperty("spring.profiles.active","dev");"* </br>
      - In the folder **../gitMedilabo/medilabo-noteapp/src/main/java/com/medilabo/medilabonoteapp**, open the file *MedilaboNoteappApplication.java* </br>
and replace : </br>
        - *"System.setProperty("spring.profiles.active","prod");"* </br>
        - by : *"System.setProperty("spring.profiles.active","dev");"* </br>
	- You have to run the command ***"mvn clean install"*** for these two services again </br>


## - **Tests**

The app has unit tests and integration tests written. </br>

After the ***"mvn clean install"*** command, in each project, you can get both Surefire and Jacoco Report 
in the folder **../target**. </br>

For Jacoco report, open the *index.html* in the folder **../jacoco**. </br>
For Surefire report, open the files in the folder **../surefire-reports**. </br>


## - **Green code suggestions**

- no superfluous features
- fluid user scenario
- only saving useful data in database
- efficient code
- refactor ot the code when possible
- using simple and appropriate formats and data structures 