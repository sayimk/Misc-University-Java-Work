<link rel='stylesheet' href='web/swiss.css'/>

# CO2006 MINI-PROJECT 16-17: base code for mini project (part 2)

You can reuse this code for your mini project (part 2).

To be able to run the application you will need to configure the class `eMarket.DbConfig.java` as explained in the lecture :movie_camera: [L9. Spring Data](https://leicester.cloud.panopto.eu/Panopto/Pages/Viewer.aspx?id=dbcfac72-69ea-4ec1-a384-42455e5c128c).

## Running a subset of scenarios in Cucumber

The task cucumber has been extended in `gradle.build` in order to enable the use of tags. For example to run only the step definitions corresponding to the annotation `@repo`, use the command: 

	./gradlew cucumber -Ptags=@repo
