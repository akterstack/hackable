package io.hackable;

public class Event<T> {

    private String name;
    private T data;

    public Event(String name, T data) {
        this.name = name;
        this.data = data;
    }

    public String name() {
        return name;
    }

    public T data() {
        return data;
    }
}
