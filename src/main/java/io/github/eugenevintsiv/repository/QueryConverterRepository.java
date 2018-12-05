package io.github.eugenevintsiv.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import io.github.eugenevintsiv.domain.Query;
import io.github.eugenevintsiv.util.QueryHelper;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class QueryConverterRepository {

    private final MongoDatabase database;

    @Autowired
    public QueryConverterRepository(MongoClient mongo, Environment env) {
        final String property = env.getProperty("spring.data.mongodb.database");
        database = mongo.getDatabase(property);
    }

    public String getData(Query query) {
//        define collection
        MongoCollection<Document> collection = database.getCollection(query.getTarget());

        QueryHelper queryHelper = new QueryHelper();
        Iterator<Document> iterator = collection
                .find(queryHelper.getDBConditions(query))
                .projection(queryHelper.getProjections(query))
                .sort(Sorts.orderBy(queryHelper.getSort(query)))
                .limit(query.getLimit())
                .skip(query.getSkip())
                .iterator();
        List<String> documents = new ArrayList<>();
        while (iterator.hasNext()) {
            documents.add(iterator.next().toJson());
        }
        return documents.toString();
    }

}