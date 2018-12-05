package io.github.eugenevintsiv.domain;

import com.mongodb.BasicDBObject;
import io.github.eugenevintsiv.exception.QueryConvertException;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTest {

    private Order order;

    @After
    public void tearDown() {
        order = null;
    }

    @Test
    public void testSuccessGettingBasicDBObject() throws QueryConvertException {
        order = new Order(0, "companySymbol", 1);
        BasicDBObject actual = order.toBasicDBObject();
        BasicDBObject expected = new BasicDBObject("companySymbol", 1);
        assertEquals(expected, actual);
    }

}