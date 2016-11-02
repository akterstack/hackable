package io.hackable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListener {

    private static final Class<? extends Hackable> GLOBAL_CONTEXT = Hackable.class;

    /* no way to initialize */
    private EventListener(){}

    private static Map<String, List<EventHandler>> mapOfHandlerHost = new HashMap<>();

    public static void on(String eventName, EventHandler eventHandler) {
        on(eventName, GLOBAL_CONTEXT, eventHandler);
    }

    public static void on(String eventName, Class<? extends Hackable> hackableClass, EventHandler eventHandler) {
        String hostKey = eventHandlerHostKey(hackableClass, eventName);
        List<EventHandler> existingHandlers = mapOfHandlerHost.get(hostKey);
        if(existingHandlers == null) {
            existingHandlers = new ArrayList<>();
            mapOfHandlerHost.put(hostKey, existingHandlers);
        }
        existingHandlers.add(eventHandler);
    }

    public static <T> void trigger(String eventName, T eventData) {
        trigger(eventName, GLOBAL_CONTEXT, eventData);
    }

    public static <T> void trigger(String eventName, Class<? extends Hackable> eventContextClass, T eventData) {
        Event<Object> event = new Event<>(eventName, eventData);
        List<EventHandler> handlers = mapOfHandlerHost.get(eventHandlerHostKey(eventContextClass, eventName));
        for(EventHandler handler : handlers) {
            handler.handle(event);
        }
    }

    private static String eventHandlerHostKey(Class<? extends Hackable> eventContextClass, String eventName) {
        if(eventContextClass == null)
            eventContextClass = GLOBAL_CONTEXT;
        return eventContextClass.getCanonicalName() + ":" + eventName;
    }
}
