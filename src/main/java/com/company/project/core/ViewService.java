package com.company.project.core;

import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Service 层 基础接口，其他Service 接口 请继承该接口(view视图版本专用)
 */
public interface ViewService<T> {
    T findBy(String fieldName, Object value) throws TooManyResultsException; // 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束

    List<T> findListBy(String fieldName, Object value); // 多结果 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束

    List<T> findByCondition(Condition condition);// 根据条件查找

    List<T> findAll();// 获取所有
}
