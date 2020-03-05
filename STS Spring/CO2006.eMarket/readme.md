<link rel='stylesheet' href='web/swiss.css'/>

# CO2006 MINI-PROJECT 16-17: sprint 2 (a solution)

This project is a solution to the :computer: [mini project worksheet](https://github.com/uol-inf/CO2006-16-17/tree/master/sprint2/mini-project) of sprint 2. The code provided in this project can be adapted, extended and reused for the mini project worksheet of sprint 3.

Moreover, since you already have experience developing web apps using Spring MVC and Cucumber, you may want to use the following productivity tips in sprint 3. Enjoy!  

## Features and productivity tips (optional)

In this solution, I have used a number of features that streamline the use of Java for developing web applications:
* Running a subset of Gherkin scenarios in Cucumber.
* [Project Lombok](https://projectlombok.org/): for removing some of the verbosity of Java programs.
* Hot swapping: for quickly redeploying Java apps.
* Reuse of CSS and Javascript libraries for web design in Spring Mvc web applications.


### Running a subset of scenarios in Cucumber

The task cucumber has been extended in `gradle.build` in order to enable the use of tags. For example to run only the step definitions corresponding to the tag `@navigation`, use the command: 

	./gradlew cucumber -Ptags=@navigation

### Project Lombok

The following are instructions have been tested on Mac OSX (for Windows/Linux, please follow equivalent instructions in the [Project Lombok](https://projectlombok.org/) web page):

* To use Lombok from the STS, you will first need to install it. Thankfully there is a gradle plugin for this. Add the following code to your gradle build script:

		// used to install lombok
		plugins {
			id 'io.franzbecker.gradle-lombok' version '1.7'
			id 'java'
		}
		// the following code may be included in your script already!
		repositories {
			mavenCentral()
		}

* Execute `./gradlew intallLombok`. This will run an installation wizard that will ask your for your IDE installation location.
* Choose the `.ini` file inside `<STS folder>/eclipse/`
* Remove the following code from the gradle build script (optional):

		// used to install lombok
		plugins {
			id 'io.franzbecker.gradle-lombok' version '1.7'
			id 'java'
		}
	
* Add the [dependency to the Lombok jar](https://mvnrepository.com/artifact/org.projectlombok/lombok) in each project where you would like to use it. For example: 

		compile group: 'org.projectlombok', name: 'lombok', version: '1.16.10'

* [Check Lombok's features](https://projectlombok.org/features/index.html) for learning about what you can do in Java now. 

### Hot swapping

When developing a web application, applying changes and redeploying the application (or even restarting it) can be quite time consuming. There are two approaches to speed up this process:
- [application restart](http://docs.spring.io/spring-boot/docs/current/reference/html/howto-hotswapping.html#howto-reload-fast-restart): fast application restart by reusing the class loader that is used to load your Java classes.
- [dynamic class reloading](http://docs.spring.io/spring-boot/docs/current/reference/html/howto-hotswapping.html#howto-reload-java-classes-without-restarting): modified classes are reloaded at a much finer level of granularity.


#### Restarting

##### From Gradle

Add the following dependency: `compile("org.springframework.boot:spring-boot-devtools:${springBootVersion}")`

Restarting the application using Gradle involves creating two processes: 
* one that runs a continuous build that will compile classes whenever there is a change using `./gradlew -t classes`
* a normal build that runs the web app using: `./gradlew bootRun`

This time, whenever there is a change in a class in the classpath of the project, the application will restart without changing the contextual libraries that have been loaded already.

#### Dynamic class reloading

##### From STS
* Download the jar file from [https://github.com/spring-projects/spring-loaded](https://github.com/spring-projects/spring-loaded) and copy it in the root folder of the project 
* Right-click on project: `Run as...Run configurations`
* Create a new Spring Boot launch configuration and append `-javaagent:springloaded-1.2.5.RELEASE.jar -noverify` in the Arguments tab (field VM arguments)
* Run your web application as a Spring Boot application from the IDE and whenever there is a change in a class, the changes will be reloaded automatically (this may still take a fraction of the time used for rebooting the application). 


##### From Gradle

Add the following code to your Gradle build script: 

		buildscript {
		    ext {
		        springBootVersion = '1.4.1.RELEASE'
		    }
		    repositories {
		        mavenCentral()
		    }
		    dependencies {
		        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}") 
				classpath("org.springframework:springloaded:1.2.5.RELEASE")
		    }
		}	

Reloading classes using Gradle involves creating two processes (usually in two different terminals): 
* one that runs a continuous build that will compile classes whenever there is a change using `./gradlew -t classes`
* a normal build that runs the web app using: `./gradlew bootRun`

Whenever you modify your Java classes in STS, the first process will **recompile them automatically** for you without having to restart the web app (in most cases). Enjoy!
 
### Reuse of CSS and Javascript libraries

You can use many CSS and javascript libraries for your designing web application by using [webjars](http://www.webjars.org/). In this example, we have used Bootstrap to draw links as buttons. To use a webjar, follow these steps:

* Add the dependency to the webjar as you would do with any Java dependency:

		compile("org.webjars:bootstrap:3.3.7")
	
* To use this dependency in a web page, use the prefix `webjars/bootstrap/3.3.7/`. For example, to use a particular CSS stylesheet, add: 

		<link rel="stylesheet" media="screen" href="webjars/bootstrap/3.3.7/css/bootstrap.min.css">

***
&copy; Artur Boronat, 2016-17