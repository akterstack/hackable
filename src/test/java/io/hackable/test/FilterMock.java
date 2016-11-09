package io.hackable.test;

import io.hackable.Hackable;

import java.util.Date;

public class FilterMock implements Hackable {

    private String name;
    private Date date;

    public FilterMock(String name) {
        this.name = name;
        this.date = new Date();
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return name + ": " + date;
    }

}
