package io.hackable;

@FunctionalInterface
public interface Handler<T> {

    void handle(T t);

}
