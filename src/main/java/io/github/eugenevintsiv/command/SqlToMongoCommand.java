package io.github.eugenevintsiv.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class SqlToMongoCommand {

    @ShellMethod(key = {"$sql", "$db"},
            value = "Convert Sql query to MongoDb query and return a result as string.")
    public String convertToMongo(@ShellOption String sqlQuery) {
        return "Hello " + sqlQuery;
    }

}
