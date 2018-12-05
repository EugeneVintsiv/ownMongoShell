package io.github.eugenevintsiv.domain;

import com.google.common.base.Objects;
import com.mongodb.BasicDBObject;
import io.github.eugenevintsiv.util.ConditionHelper;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Condition {
    private static final String AND = "and";
    private static final String OR = "or";

    private String field;
    private String operation;
    private String value;
    private String logicalOperator;
    private String prevLogicalOperator;
    private String nextLogicalOperator;

    public Condition(String field, String operation, String value, String prevLogicalOperator,
                     String nextLogicalOperator) {
        this.prevLogicalOperator = prevLogicalOperator;
        this.field = field;
        this.operation = operation;
        this.value = value;
        this.nextLogicalOperator = nextLogicalOperator;
        this.logicalOperator = defineLogicalOperator(prevLogicalOperator, nextLogicalOperator);
    }

    public Condition(String query) {
        Pattern pattern = Pattern.compile(ConditionHelper.getRegExp(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        while (matcher.find()) {
            this.prevLogicalOperator = matcher.group(1);
            this.field = matcher.group(2);
            this.operation = matcher.group(3);
            this.value = matcher.group(4);
            this.nextLogicalOperator = matcher.group(5);
            this.logicalOperator = defineLogicalOperator(prevLogicalOperator, nextLogicalOperator);
        }
    }

    private String defineLogicalOperator(String prevLogicalOperator, String nextLogicalOperator) {
        return AND.equalsIgnoreCase(prevLogicalOperator) || AND.equalsIgnoreCase(nextLogicalOperator) ? AND : OR;
    }

    public BasicDBObject getCondition() {
        return new BasicDBObject(ConditionHelper.getOperationMap().get(operation),
                NumberUtils.isCreatable(value) ? NumberUtils.createNumber(value) : value);
    }

    public String getField() {
        return field;
    }

    public String getOperation() {
        return operation;
    }

    public String getValue() {
        return value;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public String getPrevLogicalOperator() {
        return prevLogicalOperator;
    }

    public String getNextLogicalOperator() {
        return nextLogicalOperator;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(field, operation, value, logicalOperator, prevLogicalOperator, nextLogicalOperator);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Condition) {
            Condition that = (Condition) object;
            return Objects.equal(this.field, that.field) && Objects.equal(this.operation, that.operation)
                    && Objects.equal(this.value, that.value)
                    && Objects.equal(this.logicalOperator, that.logicalOperator)
                    && Objects.equal(this.prevLogicalOperator, that.prevLogicalOperator)
                    && Objects.equal(this.nextLogicalOperator, that.nextLogicalOperator);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Condition [field=" + field + ", operation=" + operation + ", value=" + value + ", logicalOperator="
                + logicalOperator + ", prevLogicalOperator=" + prevLogicalOperator + ", nextLogicalOperator="
                + nextLogicalOperator + "]";
    }

}
