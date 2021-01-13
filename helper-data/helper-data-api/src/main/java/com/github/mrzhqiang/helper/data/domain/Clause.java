package com.github.mrzhqiang.helper.data.domain;

public interface Clause {

    /**
     * EQ is name = value
     */
    String EQ = "eq";
    /**
     * NE is name != value
     */
    String NE = "ne";
    /**
     * NOT_NULL is name IS NOT NULL
     */
    String NOT_NULL = "notNull";
    /**
     * LIKE is name like value
     */
    String LIKE = "like";
    /**
     * IN is name in (value1,value2,...)
     */
    String IN = "in";
    /**
     * CONTAINS is name contains [value1,value2,...]
     */
    String CONTAINS = "contains";
    /**
     * CONTAINS KEY is name contains key value
     */
    String CONTAINS_KEY = "containsKey";
    /**
     * LT is name < value
     */
    String LT = "lt";
    /**
     * LTE is name <= value
     */
    String LTE = "lte";
    /**
     * GT is name > value
     */
    String GT = "gt";
    /**
     * GTE is name >= value
     */
    String GTE = "gte";
}
