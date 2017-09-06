package com.at.frame.mybatis;

import com.at.frame.entity.ReqInfo;

import java.util.HashSet;

/**
 * Created by Administrator on 2017/5/9.
 */
public abstract class QueryExecutorFilter {
    /**
     * 返回过滤的字段
     */
    public abstract HashSet<String> filterFields(ReqInfo requestInfo);
}
