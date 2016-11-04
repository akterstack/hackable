package io.hackable;

import java.util.Arrays;
import java.util.List;

public class Event {

    private String name;
    private List<Object> data;

    public Event(String name, Object... data) {
        this.name = name;
        this.data = Arrays.asList(data);
    }

    public String name() {
        return name;
    }

    public <T> List<T> allData() {
        return (List<T>)data;
    }

    public Object getData(int idx) {
        return data.get(idx);
    }

    @SuppressWarnings("unchecked")
    public <T> T dataAt(Integer index) {
        return (T)data.get(index);
    }

}
