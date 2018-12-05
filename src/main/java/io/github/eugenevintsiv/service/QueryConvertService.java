package io.github.eugenevintsiv.service;

import io.github.eugenevintsiv.exception.QueryConvertException;

public interface QueryConvertService {
    String convertQuery(String sqlQuery) throws QueryConvertException;
}
