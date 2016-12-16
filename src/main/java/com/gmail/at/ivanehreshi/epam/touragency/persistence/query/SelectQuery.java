package com.gmail.at.ivanehreshi.epam.touragency.persistence.query;

import java.util.Arrays;

/**
 * Implementation of {@link Query} that encapsulates various parts
 * of a SQL selection query
 */
public class SelectQuery implements Query {
    private String columns;
    private String table;
    private String[] whereClause;
    private String[] orderByClause;
    private Integer limit;

    public SelectQuery(String columns, String table) {
        this.columns = columns;
        this.table = table;
    }

    public SelectQuery(String table) {
        this.table = table;
        this.columns = "*";
    }

    public SelectQuery(SelectQuery query) {
        this.columns = query.columns;
        this.table = query.table;
        this.whereClause = copyOf(query.whereClause);
        this.orderByClause = copyOf(query.orderByClause);
        this.limit = query.limit;
    }

    @Override
    public String getSQL() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ").append(columns)
          .append(" FROM ").append(table);

        if(whereClause != null && whereClause.length > 0) {
            sb.append(" WHERE ");
            for(String s: whereClause) {
                sb.append(s).append(" ");
            }
        }

        if(orderByClause != null && orderByClause.length > 0) {
            sb.append(" ORDER BY ");

            for(String s: orderByClause) {
                sb.append(s);
            }
        }

        if(limit != null) {
            sb.append(" LIMIT ").append(limit);
        }

        return sb.toString();
    }

    public SelectQuery setColumns(String cols) {
        SelectQuery q = new SelectQuery(this);
        q.columns = cols;
        return q;
    }

    public SelectQuery setLimit(Integer limit) {
        if(limit ==  null) {
            return this;
        }

        SelectQuery q = new SelectQuery(this);
        q.limit = limit;
        return q;
    }

    public SelectQuery addOrderBy(String order) {
        SelectQuery q = new SelectQuery(this);
        q.orderByClause = append(this.orderByClause, order, String.class);
        return q;
    }

    public SelectQuery setTable(String table) {
        SelectQuery q = new SelectQuery(this);
        q.table = table;
        return q;
    }

    public SelectQuery addWhere(String wh) {
        SelectQuery q = new SelectQuery(this);
        q.whereClause = append(this.whereClause, wh, String.class);
        return q;
    }

    public String getColumns() {
        return columns;
    }

    public Integer getLimit() {
        return limit;
    }

    public String[] getOrderByClause() {
        return orderByClause;
    }

    public String getTable() {
        return table;
    }

    public String[] getWhereClause() {
        return whereClause;
    }

    private static <T>  T[] copyOf(T[] t) {
        if(t == null)
            return null;

        return Arrays.copyOf(t, t.length);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] append(T[] ts, T t, Class<T> clazz) {
        if(t == null) {
            return copyOf(ts);
        }

        if(ts == null) {
            T[] res = newArr(ts, 1, clazz);
            res[0] = t;
            return res;
        }

        T[] res = newArr(ts, ts.length + 1, clazz);
        for(int i = 0; i < ts.length; i++) {
            res[i] = ts[i];
        }

        res[ts.length] = t;
        return res;
    }

    private static <T> T[] newArr(T[] ts, int size, Class<T> clazz) {
        return (T[])java.lang.reflect.Array.newInstance(clazz, size);
    }

}
