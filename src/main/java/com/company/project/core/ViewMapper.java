package com.company.project.core;

import tk.mybatis.mapper.common.base.BaseSelectMapper;
import tk.mybatis.mapper.common.condition.SelectByConditionMapper;
import tk.mybatis.mapper.common.condition.SelectCountByConditionMapper;

/**
 * 定制版MyBatis Mapper插件接口，如需其他接口参考官方文档自行添加。(view视图版本专用)
 */
public interface ViewMapper<T> extends BaseSelectMapper<T>, SelectByConditionMapper<T>, SelectCountByConditionMapper<T> {
}
