package io.hackable;

public interface Hackable {

    default void trigger(String eventName, Object eventData) {
        EventListener.trigger(eventName, this.getClass(), eventData);
    }

    default void trigger(String eventName, Class<? extends Hackable> eventContextClass, Object eventData) {
        EventListener.trigger(eventName, eventContextClass, eventData);
    }

}
