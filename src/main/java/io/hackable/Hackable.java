package io.hackable;

public interface Hackable {

    default void trigger(String eventName, Object... eventData) {
        trigger(eventName, this.getClass(), eventData);
    }

    default void trigger(String eventName, Class<? extends Hackable> eventContextClass, Object... eventData) {
        Events.trigger(eventName, eventContextClass, eventData);
    }

    default <R> R applyFilter(String filterName, R filterable) {
        return applyFilter(filterName, this.getClass(), filterable);
    }

    default <R> R applyFilter(String filterName, Class<? extends Hackable> contextClass, R filterable) {
        return Filters.applyFilter(filterName, contextClass, filterable);
    }

}
