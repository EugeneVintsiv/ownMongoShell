package io.github.eugenevintsiv.util;

import static io.github.eugenevintsiv.util.QueryConverterUtils.ErrorMsg.CONDITION_ERR_MSG;
import static io.github.eugenevintsiv.util.QueryConverterUtils.ErrorMsg.FIELDS_ERR_MSG;
import static io.github.eugenevintsiv.util.QueryConverterUtils.ErrorMsg.LIMIT_ERR_MSG;
import static io.github.eugenevintsiv.util.QueryConverterUtils.ErrorMsg.ORDERS_ERR_MSG;
import static io.github.eugenevintsiv.util.QueryConverterUtils.ErrorMsg.SKIP_ERR_MSG;
import static io.github.eugenevintsiv.util.QueryConverterUtils.ErrorMsg.TARGET_ERR_MSG;
import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.CONDITIONS_REGEXP;
import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.FIELDS_REGEXP;
import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.LIMIT_REGEXP;
import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.ORDERS_REGEXP;
import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.SKIP_REGEXP;
import static io.github.eugenevintsiv.util.QueryConverterUtils.QueryRegexp.TARGET_REGEXP;

public enum SqlQueryParts {

    FIELD(FIELDS_REGEXP, FIELDS_ERR_MSG),
    TARGET(TARGET_REGEXP, TARGET_ERR_MSG),
    CONDITION(CONDITIONS_REGEXP, CONDITION_ERR_MSG),
    ORDER(ORDERS_REGEXP, ORDERS_ERR_MSG),
    LIMIT(LIMIT_REGEXP, LIMIT_ERR_MSG),
    SKIP(SKIP_REGEXP, SKIP_ERR_MSG);

    private SqlQueryParts(String re, String errMsg) {
        this.REGEXP = re;
        this.ERR_MSG = errMsg;
    }

    public final String REGEXP;
    public final String ERR_MSG;


}
