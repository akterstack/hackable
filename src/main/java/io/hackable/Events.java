package io.hackable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Events {

    private static final Class<? extends Hackable> GLOBAL_CONTEXT = Hackable.class;

    private final static Map<String, List<Consumer<Event>>> eventHandlersMap = new HashMap<>();

    /* no way to initialize */
    private Events(){}

    public static void on(String eventName, Consumer<Event> eventHandler) {
        on(eventName, GLOBAL_CONTEXT, eventHandler);
    }

    public static void on(String eventName, Class<? extends Hackable> hackableClass, Consumer<Event> eventHandler) {
        String hostKey = resolveEventHandlerKey(hackableClass, eventName);
        List<Consumer<Event>> existingHandlers = eventHandlersMap.get(hostKey);
        if(existingHandlers == null) {
            existingHandlers = new ArrayList<>();
            eventHandlersMap.put(hostKey, existingHandlers);
        }
        existingHandlers.add(eventHandler);
    }

    public static <T> void trigger(String eventName, T... eventData) {
        trigger(eventName, GLOBAL_CONTEXT, eventData);
    }

    public static <T> void trigger(String eventName, Class<? extends Hackable> eventContextClass, T... eventData) {
        Event event = new Event(eventName, eventData);
        List<Consumer<Event>> consumers = eventHandlersMap.get(resolveEventHandlerKey(eventContextClass, eventName));
        if(consumers != null) {
            for(Consumer<Event> consumer : consumers) {
                consumer.accept(event);
            }
        }
    }

    private static String resolveEventHandlerKey(Class<? extends Hackable> eventContextClass, String eventName) {
        if(eventContextClass == null)
            eventContextClass = GLOBAL_CONTEXT;
        return eventContextClass.getCanonicalName() + ":" + eventName;
    }
}
