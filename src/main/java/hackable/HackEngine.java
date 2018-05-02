package hackable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public final class HackEngine {

  private static final Map<String, List<EventHandler<?>>> eventHandlersHostMap = new HashMap<>();
  private static final Map<String, List<Filter<?>>> filterHandlersHostMap = new HashMap<>();

  private HackEngine() {}

  public static <T> void registerEventHandler(String eventName, EventHandler<T> eventEventHandler) {
    registerEventHandler(eventName, eventEventHandler, eventHandlersHostMap);
  }

  public static <T> void registerEventHandler(
      String eventName,
      EventHandler<T> eventEventHandler,
      Map<String, List<EventHandler<?>>> eventHandlersHostMap) {

    List<EventHandler<?>> existingEventHandlers =
        eventHandlersHostMap
            .computeIfAbsent(eventName, k -> new ArrayList<>());
    existingEventHandlers.add(eventEventHandler);
  }

  public static <T> void doEvent(String eventName, T eventData) {
    doEvent(eventName, eventData, eventHandlersHostMap);
  }

  public static <T> void doEvent(
      String eventName, T eventData, Map<String, List<EventHandler<?>>> eventHandlersHostMap) {
    List<EventHandler<?>> events = eventHandlersHostMap.get(eventName);
    if(events != null) {
      for(EventHandler event : events) {
        event.handle(eventData);
      }
    }
  }

  public static <R> void registerFilterHandler(String filterName, Filter<R> filterHandler) {
    registerFilterHandler(filterName, filterHandler, filterHandlersHostMap);
  }

  public static <R> void registerFilterHandler(
      String filterName, Filter<R> filterHandler, Map<String, List<Filter<?>>> filterHandlersHostMap) {
    String filterHostKey = filterName.toLowerCase();
    List<Filter<?>> existingHandlers =
        filterHandlersHostMap.computeIfAbsent(filterHostKey, k -> new LinkedList<>());
    existingHandlers.add(filterHandler);
  }

  public static <R> R applyFilter(String filterName, R filterableObject) {
    return applyFilter(filterName, filterableObject, filterHandlersHostMap);
  }

  public static <R> R applyFilter(
      String filterName, R filterableObject, Map<String, List<Filter<?>>> filterHandlersHostMap) {

    List<Filter<?>> filters = filterHandlersHostMap.get(filterName.toLowerCase());
    if(filters != null) {
      R fobj = filterableObject;
      for(Filter f : filters) {
        fobj = (R)f.filter(fobj);
      }
      return fobj;
    }
    return filterableObject;
  }

}
