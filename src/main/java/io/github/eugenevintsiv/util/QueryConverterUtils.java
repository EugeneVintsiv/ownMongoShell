package io.github.eugenevintsiv.util;

public class QueryConverterUtils {
	public class QueryRegexp {
		public static final String CONDITION_REGEXP = "(?=((?:^|(?:(?:and|or)\\s+))(?:\\w+(?:\\.\\w+)?\\s*(?:<|<=|=|<>|>=|>)\\s*\\'?\\\"?\\s*\\w+(?:\\.\\w+)?)\\s*\\\"?\\'?(?:(?:\\s+(?:AND|OR)|$))))";
		public static final String WHERE_REGEXP = "(?i).*WHERE.*";
		public static final String ORDER_BY_REGEXP = "(?i).*ORDER\\s+BY.*";

		static final String FIELDS_REGEXP = "SELECT\\s+(((\\w+\\.)?[\\w\\*]+)\\s*(\\,\\s*(\\w+\\.)?[\\w\\*]+)*)(\\s+FROM)";
		static final String TARGET_REGEXP = "FROM\\s+(\\w+?)\\s*((WHERE|GROUP BY|ORDER BY|LIMIT|SKIP)|$)";
		static final String CONDITIONS_REGEXP = "WHERE\\s+((\\w+(\\.\\w+)?\\s*(>|>=|=|<>|<=|<)\\s*\\'?\\\"?\\w+(\\.\\w+)?\\'?\\\"?)(\\s+(AND|OR)(\\s+\\w+(\\.\\w+)?\\s*(>|>=|=|<>|<=|<)\\s*\\'?\\\"?\\w+(\\.\\w+)?\\'?\\\"?))*)\\s*(GROUP BY|ORDER BY|LIMIT|SKIP|$)";
		static final String ORDERS_REGEXP = "ORDER\\s+BY\\s+((\\w+(\\.\\w+)?(\\s+(ASC|DESC))?)(\\s*\\,\\s*\\w+(\\.\\w+)?(\\s+(ASC|DESC))?)*)(\\s+(LIMIT|SKIP)|$)";
		static final String LIMIT_REGEXP = "LIMIT\\s+(\\d+)\\s*(SKIP|$)";
		static final String SKIP_REGEXP = "SKIP\\s+(\\d+)\\s*$";
	}

	class ErrorMsg {
		static final String FIELDS_ERR_MSG = "Incorrect query: Query should contain fields for selecting";
		static final String TARGET_ERR_MSG = "Incorrect query: Query should contain table name";
		static final String CONDITION_ERR_MSG = "Incorrect query: Incorrect WHERE condition";
		static final String ORDERS_ERR_MSG = "Incorrect query: Incorrect ORDER condition";
		static final String LIMIT_ERR_MSG = "Incorrect query: Incorrect LIMIT value";
		static final String SKIP_ERR_MSG = "Incorrect query: Incorrect SKIP value";
	}

	private QueryConverterUtils() {
	}

}