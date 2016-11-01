package io.hackable;

public interface Hackable {

    default void trigger(String eventName, Object eventData) {
        EventListener.trigger(eventName, this.getClass(), eventData);
    }

}
