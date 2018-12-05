package io.github.eugenevintsiv.util;

import java.util.HashMap;
import java.util.Map;

public class ConditionHelper {

    private static final String REG_EXP = "(AND|OR)?\\s*(\\w+(?:\\.\\w+)?)\\s*(<|<=|=|<>|>=|>)\\s*\\'?\\\"?(\\w+(?:\\.\\w+)?)\\\"?\\'?\\s*(AND|OR)?";
    private static final Map<String, String> OPERATIONS = new HashMap<>();

    static {
        OPERATIONS.put("<", "$lt");
        OPERATIONS.put("<=", "$lte");
        OPERATIONS.put("<>", "$ne");
        OPERATIONS.put("=", "$eq");
        OPERATIONS.put(">=", "$gte");
        OPERATIONS.put(">", "$gt");
    }

    private ConditionHelper() {
    }

    public static String getRegExp() {
        return REG_EXP;
    }

    public static Map<String, String> getOperationMap() {
        return OPERATIONS;
    }

}
