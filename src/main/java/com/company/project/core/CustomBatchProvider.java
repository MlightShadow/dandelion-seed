package com.company.project.core;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Set;

/**
 * SpecialProvider实现类，特殊方法实现类
 */
public class CustomBatchProvider extends MapperTemplate {

    public CustomBatchProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 批量插入
     *
     * @param ms
     */
    public String insertList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        // 开始拼sql
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
        sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
        sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
        // 获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        // 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            if (column.isInsertable()) {
                sql.append(column.getColumnHolder("record") + ",");
            }
        }
        sql.append("</trim>");
        sql.append("</foreach>");
        return sql.toString();
    }

    /**
     * 批量更新
     *
     * @param ms
     */
    public String updateList(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();

        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        sql.append("<trim prefix=\"set\" suffixOverrides=\",\">");
        // 获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        // 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {

            if (!column.isId() && column.isUpdatable()) {
                sql.append("<trim prefix=\" " + column.getColumn() + " =case\" suffix=\"end,\">");
                sql.append("<foreach collection=\"list\" item=\"record\">");
                sql.append("<if test=\"");
                if (StringUtil.isNotEmpty("record")) {
                    sql.append("record").append(".");
                }
                sql.append(column.getProperty()).append(" != null");
                if (isNotEmpty() && column.getJavaType().equals(String.class)) {
                    sql.append(" and ");
                    if (StringUtil.isNotEmpty("record")) {
                        sql.append("record").append(".");
                    }
                    sql.append(column.getProperty()).append(" != '' ");
                }
                sql.append("\">");
                sql.append(" WHEN id=#{record.id} THEN " + column.getColumnHolder("record") + " ");
                sql.append("</if>");
                sql.append("</foreach>");
                sql.append("</trim>");
            }
        }
        sql.append("</trim>");

        sql.append(" where id in");
        Set<EntityColumn> pkList = EntityHelper.getPKColumns(entityClass);

        sql.append("<foreach collection=\"list\" item=\"record\" open=\"(\" separator=\",\" close=\")\">");
        for (EntityColumn pk : pkList) {
            sql.append(pk.getColumnHolder("record"));
        }
        sql.append("</foreach>");
        return sql.toString();
    }
}
