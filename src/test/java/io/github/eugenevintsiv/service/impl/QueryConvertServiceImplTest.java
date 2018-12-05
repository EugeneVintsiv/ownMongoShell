package io.github.eugenevintsiv.service.impl;

import com.mongodb.MongoClient;
import io.github.eugenevintsiv.domain.Condition;
import io.github.eugenevintsiv.domain.Order;
import io.github.eugenevintsiv.exception.QueryConvertException;
import io.github.eugenevintsiv.repository.QueryConverterRepository;
import io.github.eugenevintsiv.util.PatternCacheHolder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.CONDITION_REGEXP;
import static io.github.eugenevintsiv.util.SqlQueryParts.CONDITION;
import static io.github.eugenevintsiv.util.SqlQueryParts.FIELD;
import static io.github.eugenevintsiv.util.SqlQueryParts.LIMIT;
import static io.github.eugenevintsiv.util.SqlQueryParts.ORDER;
import static io.github.eugenevintsiv.util.SqlQueryParts.SKIP;
import static io.github.eugenevintsiv.util.SqlQueryParts.TARGET;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class QueryConvertServiceImplTest {
    @Mock
    PatternCacheHolder patternCacheHolder;

    @Mock
    private MongoClient mongoClient;

    @Mock
    QueryConverterRepository repository;

    @InjectMocks
    private QueryConvertServiceImpl service;

    @Before
    public void setup() {
        Mockito.when(patternCacheHolder.getPattern(FIELD.REGEXP)).thenReturn(compile(FIELD.REGEXP, CASE_INSENSITIVE));
        Mockito.when(patternCacheHolder.getPattern(TARGET.REGEXP)).thenReturn(compile(TARGET.REGEXP, CASE_INSENSITIVE));
        Mockito.when(patternCacheHolder.getPattern(CONDITION.REGEXP)).thenReturn(compile(CONDITION.REGEXP, CASE_INSENSITIVE));
        Mockito.when(patternCacheHolder.getPattern(ORDER.REGEXP)).thenReturn(compile(ORDER.REGEXP, CASE_INSENSITIVE));
        Mockito.when(patternCacheHolder.getPattern(LIMIT.REGEXP)).thenReturn(compile(LIMIT.REGEXP, CASE_INSENSITIVE));
        Mockito.when(patternCacheHolder.getPattern(SKIP.REGEXP)).thenReturn(compile(SKIP.REGEXP, CASE_INSENSITIVE));
        Mockito.when(patternCacheHolder.getPattern(CONDITION_REGEXP)).thenReturn(compile(CONDITION_REGEXP, CASE_INSENSITIVE));
    }

    @Test(expected = QueryConvertException.class)
    public void testFailConvertOnMissedSelect() throws QueryConvertException {
        String query = "* FROM WHERE COMPANY_SYMBOL=\"FB\"";
        service.parseTarget(query);
    }

    @Test(expected = QueryConvertException.class)
    public void testFailDefineTableOnMissedTableName() throws QueryConvertException {
        String query = "SELECT * FROM WHERE COMPANY_SYMBOL=\"FB\"";
        service.parseTarget(query);
    }

    @Test
    public void testSuccessDefineTable() throws QueryConvertException {
        String query = "SELECT * FROM MARKET WHERE COMPANY_SYMBOL=\"FB\"";
        String expected = "MARKET";
        String actual = service.parseTarget(query);
        assertEquals(expected, actual);
    }

    @Test(expected = QueryConvertException.class)
    public void testFailDefineOnMissedFields() throws QueryConvertException {
        String query = "SELECT  FROM MARKET ORDER BY AGE";
        service.parseFields(query);
    }

    @Test
    public void testSuccessParseOneField() throws QueryConvertException {
        String query = "SELECT ADDRESS.* FROM MARKET ORDER BY AGE";
        List<String> expected = Collections.singletonList("ADDRESS");
        List<String> actual = service.parseFields(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessParseMultipleFields() throws QueryConvertException {
        String query = "SELECT FIELD_1, FIELD_2 FROM MARKET ORDER BY AGE";
        List<String> expected = Arrays.asList("FIELD_1", "FIELD_2");
        List<String> actual = service.parseFields(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessExtractStarFromField() throws QueryConvertException {
        String query = "SELECT ADDRESS.*, AGE, * FROM MARKET ORDER BY AGE";
        List<String> expected = Arrays.asList("ADDRESS", "AGE", "*");
        List<String> actual = service.parseFields(query);
        assertEquals(expected, actual);
    }

    @Test(expected = QueryConvertException.class)
    public void testFailParseOrderOnMissedOrderCondition() throws QueryConvertException {
        String query = "SELECT * FROM MARKET ORDER BY";
        service.parseOrders(query);
    }

    @Test(expected = QueryConvertException.class)
    public void testFailParseOrderOnWrongORderCondition() throws QueryConvertException {
        String query = "SELECT *  FROM MARKET ORDER BY NAME DES";
        service.parseOrders(query);
    }

    @Test
    public void testSuccessParseOrder() throws QueryConvertException {
        String query = "SELECT * FROM MARKET ORDER BY AGE";
        List<Order> expected = Arrays.asList(new Order(0, "AGE", 1));
        List<Order> actual = service.parseOrders(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessParseMultiplyOrders() throws QueryConvertException {
        String query = "SELECT FIELD_1, FIELD_2 FROM MARKET ORDER BY FIELD_1 ASC, ANOTHER_TABLE.COLUMN DESC, FIELD_3";
        List<Order> expected = Arrays.asList(new Order(0, "FIELD_1", 1), new Order(1, "ANOTHER_TABLE.COLUMN", -1),
                new Order(2, "FIELD_3", 1));
        List<Order> actual = service.parseOrders(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessParseNoOrder() throws QueryConvertException {
        String query = "SELECT * FROM MARKET";
        List<Order> expected = Collections.emptyList();
        List<Order> actual = service.parseOrders(query);
        assertEquals(expected, actual);
    }

    @Test(expected = QueryConvertException.class)
    public void testFailParseLimitOnWrongCondition() throws QueryConvertException {
        String query = "SELECT * FROM MARKET LIMIT";
        service.parseInt(query, LIMIT);
    }

    @Test
    public void testSuccessParseLimit() throws QueryConvertException {
        String query = "SELECT * FROM BRAND WHERE AGE > 10 LIMIT 10 SKIP 5";
        int expected = 10;
        int actual = service.parseInt(query, LIMIT);
        assertEquals(expected, actual);
    }

    @Test(expected = QueryConvertException.class)
    public void testFailParseSkipOnWrongCondition() throws QueryConvertException {
        String query = "SELECT * FROM MARKET LIMIT 3 SKIP";
        service.parseInt(query, SKIP);
    }

    @Test
    public void testSuccessParseSkip() throws QueryConvertException {
        String query = "SELECT * FROM BRAND WHERE AGE > 10 LIMIT 10 SKIP 5";
        int expected = 5;
        int actual = service.parseInt(query, SKIP);
        assertEquals(expected, actual);
    }

    @Test(expected = QueryConvertException.class)
    public void testFailureParseWhereOnWrongConditions() throws QueryConvertException {
        String query = "SELECT * FROM BRAND WHERE LIMIT 10 SKIP 5";
        service.parseCondition(query);
    }

    @Test(expected = QueryConvertException.class)
    public void testFailureParseWhereWithIncorrectConditions() throws QueryConvertException {
        String query = "SELECT * FROM BRAND WHERE A LIMIT 10 SKIP 5";
        service.parseCondition(query);
    }

    @Test
    public void testSuccessParseWithoutWhere() throws QueryConvertException {
        String query = "SELECT * FROM BRAND LIMIT 10 SKIP 5";
        List<Condition> expected = Collections.emptyList();
        List<Condition> actual = service.parseCondition(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessParseSingleWhereCondition() throws QueryConvertException {
        String query = "SELECT * FROM BRAND WHERE ABBR = \"APPLE\" LIMIT 10 SKIP 5";
        List<Condition> expected = Arrays.asList(new Condition("ABBR", "=", "APPLE", null, null));
        List<Condition> actual = service.parseCondition(query);
        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessParseMultipleWhereConditions() throws QueryConvertException {
        String query = "SELECT * FROM BRAND WHERE ABBR = \"APPLE\" AND PRICE > 500000 OR ADDRESS.COUNTRY <> \"USA\" LIMIT 10 SKIP 5";
        List<Condition> expected = Arrays.asList(
                new Condition("ABBR", "=", "APPLE", null, "AND"),
                new Condition("PRICE", ">", "500000", "AND", "OR"),
                new Condition("ADDRESS.COUNTRY", "<>", "USA", "OR", null));
        List<Condition> actual = service.parseCondition(query);
        assertEquals(expected, actual);
    }
}