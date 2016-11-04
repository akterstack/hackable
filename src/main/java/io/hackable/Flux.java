package io.hackable;

public interface Flux {

    Object[] objs = new Object[10];

    Object[] handle(Object... args);

    default <T> T get(int idx) {
        return (T)objs[idx];
    }

}
