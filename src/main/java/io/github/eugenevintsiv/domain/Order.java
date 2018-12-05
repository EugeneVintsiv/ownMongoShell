package io.github.eugenevintsiv.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.mongodb.BasicDBObject;

public class Order implements Comparable<Order> {
    private static final String DESC = "DESC";
    private int orderNum;
    private String field;
    private int direction;

    public Order(int orderNum, String field, int direction) {
        this.orderNum = orderNum;
        this.field = field;
        this.direction = direction;
    }

    public Order(int orderNum, String orderParts) {
        String[] order = orderParts.trim().split("\\s+");
        this.orderNum = orderNum;
        this.field = order[0];
        this.direction = (order.length) > 1 && DESC.equalsIgnoreCase(order[1]) ? -1 : 1;
    }

    public BasicDBObject toBasicDBObject() {
        return new BasicDBObject(field, direction);
    }

    public int getOrderNum() {
        return orderNum;
    }

    public String getField() {
        return field;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return orderNum == order.orderNum &&
                direction == order.direction &&
                Objects.equal(field, order.field);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderNum, field, direction);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("orderNum", orderNum)
                .add("field", field)
                .add("direction", direction)
                .toString();
    }

    @Override
    public int compareTo(Order that) {
        return ComparisonChain.start().compare(this.orderNum, that.orderNum).result();
    }

}
