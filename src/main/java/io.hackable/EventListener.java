package io.hack;

@FunctionalInterface
public interface EventListener {

    void listen(Event event);

}
