package com.futurama.serenity.utils;

import java.util.List;

/**
 * Created by wilfried on 04/01/2016.
 */
public class GenericListResult<T> {
    private int total;

    private List<T> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "GenericList{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
