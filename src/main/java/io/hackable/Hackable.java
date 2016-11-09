package io.hackable;

public interface Hackable {

    default void trigger(String eventName, Object... eventData) {
        Events.trigger(eventName, this.getClass(), eventData);
    }

    default void trigger(String eventName, Class<? extends Hackable> eventContextClass, Object... eventData) {
        Events.trigger(eventName, eventContextClass, eventData);
    }

    default Object applyFilter(String filterName, Object filterable) {

    }


}
