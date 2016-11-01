package io.hackable;

import java.util.HashMap;
import java.util.Map;

public class EventListener {

    /* no way to initialize */
    private EventListener(){}

    private static Map<Class<? extends Hackable>, Map<String, EventHandler>> handlers = new HashMap<>();

    public static void on(String eventName, EventHandler eventHandler) {
        on(eventName, Hackable.class, eventHandler);
    }

    public static void on(String eventName, Class<? extends Hackable> hackableClass, EventHandler eventHandler) {
        Map<String, EventHandler> namedHandlers = handlers.get(hackableClass);
        if(namedHandlers == null) {
            namedHandlers = new HashMap<>();
            handlers.put(hackableClass, namedHandlers);
        }
        namedHandlers.put(eventName, eventHandler);
    }

    public static void trigger(String eventName, Class<? extends Hackable> eventContextClass, Object eventData) {
        Event event = new Event(eventName, eventData);
        Map<String, EventHandler> namedHandlers = handlers.get(eventContextClass);
        EventHandler handler = namedHandlers.get(eventName);
        handler.handle(event);
    }

}
