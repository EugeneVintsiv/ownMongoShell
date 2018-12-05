package io.github.eugenevintsiv.domain;

import com.mongodb.BasicDBObject;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConditionTest {

    private Condition condition;

    @After
    public void tearDown() {
        condition = null;
    }

    @Test
    public void testSuccessGettingStringCondition() {
        condition = new Condition("companySymbol", "=", "FB", null, null);
        BasicDBObject actual = condition.getCondition();
        BasicDBObject expected = new BasicDBObject("$eq", "FB");
        assertEquals(expected, actual);
    }

    @Test
    public void testSuccessGettingNumberCondition() {
        condition = new Condition("number", "<", "12", null, null);
        BasicDBObject actual = condition.getCondition();
        BasicDBObject expected = new BasicDBObject("$lt", 12);
        assertEquals(expected, actual);
    }

}