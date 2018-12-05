package io.github.eugenevintsiv.command;

import io.github.eugenevintsiv.exception.QueryConvertException;
import io.github.eugenevintsiv.service.QueryConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class SqlToMongoCommand {

    private final QueryConvertService queryConvertService;

    @Autowired
    public SqlToMongoCommand(QueryConvertService queryConvertService) {
        this.queryConvertService = queryConvertService;
    }

    @ShellMethod(key = {"$sql", "$db"},
            value = "Convert Sql query to MongoDb query and return a result as string. Example: $sql 'your select query'")
    public String convertToMongo(@ShellOption String sqlQuery) {
        try {
            return queryConvertService.convertQuery(sqlQuery.trim());
        } catch (QueryConvertException e) {
            return e.getMessage();
        }
    }
}
