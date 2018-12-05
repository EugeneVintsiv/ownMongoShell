package io.github.eugenevintsiv.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;

public class Query {
    private List<String> fields;
    private String target;
    private List<Condition> conditions;
    private List<Order> orders;
    private int limit;
    private int skip;

    public List<String> getFields() {
        return fields;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public int getLimit() {
        return limit;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public int getSkip() {
        return skip;
    }

    public String getTarget() {
        return target;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Query)) return false;
        Query query = (Query) o;
        return limit == query.limit &&
                skip == query.skip &&
                Objects.equal(fields, query.fields) &&
                Objects.equal(target, query.target) &&
                Objects.equal(conditions, query.conditions) &&
                Objects.equal(orders, query.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fields, target, conditions, orders, limit, skip);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fields", fields)
                .add("target", target)
                .add("conditions", conditions)
                .add("orders", orders)
                .add("limit", limit)
                .add("skip", skip)
                .toString();
    }
}
