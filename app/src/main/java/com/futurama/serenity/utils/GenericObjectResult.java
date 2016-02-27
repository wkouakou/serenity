package com.futurama.serenity.utils;

import java.util.List;

/**
 * Created by wilfried on 04/01/2016.
 */
public class GenericObjectResult<T> {

    private long total;

    private T row;

    private List<T> rows;

    private Object result;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getRow() {
        return row;
    }

    public void setRow(T row) {
        this.row = row;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
