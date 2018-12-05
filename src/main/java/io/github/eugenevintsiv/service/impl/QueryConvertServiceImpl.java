package io.github.eugenevintsiv.service.impl;

import io.github.eugenevintsiv.domain.Condition;
import io.github.eugenevintsiv.domain.Order;
import io.github.eugenevintsiv.domain.Query;
import io.github.eugenevintsiv.exception.QueryConvertException;
import io.github.eugenevintsiv.repository.QueryConverterRepository;
import io.github.eugenevintsiv.service.QueryConvertService;
import io.github.eugenevintsiv.util.PatternCacheHolder;
import io.github.eugenevintsiv.util.SqlQueryParts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.CONDITION_REGEXP;
import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.ORDER_BY_REGEXP;
import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.WHERE_REGEXP;
import static io.github.eugenevintsiv.util.SqlQueryParts.CONDITION;
import static io.github.eugenevintsiv.util.SqlQueryParts.FIELD;
import static io.github.eugenevintsiv.util.SqlQueryParts.LIMIT;
import static io.github.eugenevintsiv.util.SqlQueryParts.ORDER;
import static io.github.eugenevintsiv.util.SqlQueryParts.SKIP;
import static io.github.eugenevintsiv.util.SqlQueryParts.TARGET;

@Service
public class QueryConvertServiceImpl implements QueryConvertService {

    public final PatternCacheHolder patternCacheHolder;
    public final QueryConverterRepository repository;

    @Autowired
    public QueryConvertServiceImpl(PatternCacheHolder patternCacheHolder,
                                   QueryConverterRepository repository) {
        this.patternCacheHolder = patternCacheHolder;
        this.repository = repository;
    }

    @Override
    public String convertQuery(String sqlQuery) throws QueryConvertException {
        Query query = parseSQLQuery(sqlQuery);
        return repository.getData(query);
    }

    public Query parseSQLQuery(String sqlQuery) throws QueryConvertException {
        Query query = new Query();
        query.setFields(parseFields(sqlQuery));
        query.setTarget(parseTarget(sqlQuery));
        query.setConditions(parseCondition(sqlQuery));
        query.setOrders(parseOrders(sqlQuery));
        query.setLimit(parseInt(sqlQuery, LIMIT));
        query.setSkip(parseInt(sqlQuery, SKIP));
        return query;
    }

    public List<Condition> parseCondition(String sqlQuery) throws QueryConvertException {
        if (sqlQuery.matches(WHERE_REGEXP)) {
            String condition = parseStringFromSQL(sqlQuery, CONDITION);
            return parseConditionParts(condition);
        }
        return Collections.emptyList();
    }

    public List<Condition> parseConditionParts(String condition) {
        Pattern pattern = patternCacheHolder.getPattern(CONDITION_REGEXP);
        Matcher matcher = pattern.matcher(condition);
        List<Condition> conditions = new ArrayList<>();
        while (matcher.find()) {
            conditions.add(new Condition(matcher.group(1)));
        }
        return conditions;
    }

    public List<String> parseFields(String sqlQuery) throws QueryConvertException {
        String rawFields = parseStringFromSQL(sqlQuery, FIELD);
        return Arrays.stream(rawFields.split(","))
                .map(e -> e.split("\\.\\*")[0].trim())
                .collect(Collectors.toList());
    }

    public String parseTarget(String sqlQuery) throws QueryConvertException {
        return parseStringFromSQL(sqlQuery, TARGET);
    }

    public List<Order> parseOrders(String sqlQuery) throws QueryConvertException {
        if (sqlQuery.matches(ORDER_BY_REGEXP)) {
            String[] orderParts = parseStringFromSQL(sqlQuery, ORDER).split(",");
            return IntStream.range(0, orderParts.length).mapToObj(i -> new Order(i, orderParts[i]))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public String parseStringFromSQL(String sqlQuery, SqlQueryParts qp) throws QueryConvertException {
        Pattern pattern = patternCacheHolder.getPattern(qp.REGEXP);
        Matcher matcher = pattern.matcher(sqlQuery);
        matcher.find();
        String value;
        try {
            value = matcher.group(1);
        } catch (IllegalStateException e) {
            throw new QueryConvertException(qp.ERR_MSG);
        }
        return value;
    }

    public int parseInt(String sqlQuery, SqlQueryParts queryPart) throws QueryConvertException {
        return sqlQuery.matches("(?i).*" + queryPart.toString() + ".*")
                ? Integer.parseInt(parseStringFromSQL(sqlQuery, queryPart)) : 0;
    }


}
