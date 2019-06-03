package com.company.project.core;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;

public interface UpdateListMapper<T> {

    @Options(keyProperty = "id")
    @UpdateProvider(type = CustomBatchProvider.class, method = "dynamicSQL")
    int updateList(List<T> recordList);
}