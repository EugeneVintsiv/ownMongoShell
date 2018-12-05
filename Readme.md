Sql query to MongoDB convertion project.

The project is based on Spring Boot, so all solutions work for this project.

Dependencies:
spring-shell
guava
apache-commons-lang3
mongodb-driver
junit
mockito

How to run:
1) Download sources and compile project
2) possible to run via command line: java -jar OwnMongoShell-1.0.0.RELEASE.jar 
or
3) Download project from Git in the Intellij idea
4) Run app from SpringShellSample.java on main 

How to use: 
As common Spring Shell apps, this app has help command where you can see supported commands.
To check sqlToMongo conversion need to run next, example:
$sql "select * from table"

Default mongo db connection properties:
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=query_converter

Please specify your configs in a property file or jvm option