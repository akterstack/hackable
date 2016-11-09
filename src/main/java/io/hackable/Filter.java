package io.hackable;

public class Filter {

    private String name;
    private Object filterableObject;

    public Filter(String name, Object filterableObject) {
        this.name = name;
        this.filterableObject = filterableObject;
    }

    public String name() {
        return name;
    }

    public Object filterableObject() {
        return filterableObject;
    }

}
