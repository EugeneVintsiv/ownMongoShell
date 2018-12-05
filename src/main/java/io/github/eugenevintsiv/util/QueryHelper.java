package io.github.eugenevintsiv.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import io.github.eugenevintsiv.domain.Condition;
import io.github.eugenevintsiv.domain.Order;
import io.github.eugenevintsiv.domain.Query;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.stream.Collectors;

public class QueryHelper {

    private static final String AND = "and";
    private static final String OR = "or";
    private static final String MONGO_OR = "$or";
    private static final String MONGO_AND = "$and";
    private BasicDBList andList = new BasicDBList();

    public BasicDBObject getProjections(Query query) {
        BasicDBObject proj = new BasicDBObject();
        proj.append("_id", 0);
        if (query.getFields().contains("*")) {
            return proj;
        }
        query.getFields().forEach(e -> proj.append(e, 1));
        return proj;
    }

    public BasicDBObject getDBConditions(Query query) {
        BasicDBObject condition = new BasicDBObject();
        QueryHelper qb = new QueryHelper();
        BasicDBList conditionList = new BasicDBList();
        query.getConditions().forEach(e -> qb.addConditions(conditionList, e));
        return conditionList.isEmpty() ? new BasicDBObject() : condition.append(MONGO_OR, conditionList);
    }

    private void addConditions(BasicDBList conditionList, Condition condition) {
        if (isAndOperator(condition.getLogicalOperator())) {
            if (isAndOperator(condition.getPrevLogicalOperator())) {
                andList.add(new BasicDBObject(condition.getField(), condition.getCondition()));
            } else {
                andList = new BasicDBList();
                andList.add(new BasicDBObject(condition.getField(), condition.getCondition()));
            }
            if (condition.getNextLogicalOperator() == null || isOrOperator(condition.getNextLogicalOperator())) {
                conditionList.add(new BasicDBObject(MONGO_AND, andList));
            }
            return;
        }
        conditionList.add(new BasicDBObject(condition.getField(), condition.getCondition()));
    }

    public List<Bson> getSort(Query query) {
        return query.getOrders().stream().sorted().map(Order::toBasicDBObject).collect(Collectors.toList());
    }

    private boolean isAndOperator(String pretender) {
        return AND.equalsIgnoreCase(pretender);
    }

    private boolean isOrOperator(String pretender) {
        return OR.equalsIgnoreCase(pretender);
    }


}