package com.company.project.core;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

public interface InsertListMapper<T> {
    @Options(useGeneratedKeys = false, keyProperty = "id")
    @InsertProvider(type = CustomBatchProvider.class, method = "dynamicSQL")
    int insertList(List<T> recordList);
}