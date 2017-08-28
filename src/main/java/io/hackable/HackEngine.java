package io.hackable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public final class HackEngine {

  private static final Map<String, List<Handler<?>>> actionHandlersHostMap = new HashMap<>();
  private static final Map<String, List<Filter<?>>> filterHandlersHostMap = new HashMap<>();

  private HackEngine() {}

  public static <T> void registerActionHandler(String actionName, Handler<T> actionHandler) {
    registerActionHandler(actionName, actionHandler, actionHandlersHostMap);
  }

  public static <T> void registerActionHandler(
      String actionName,
      Handler<T> actionHandler,
      Map<String, List<Handler<?>>> actionHandlersHostMap) {

    List<Handler<?>> existingHandlers =
        actionHandlersHostMap
            .computeIfAbsent(actionName, k -> new ArrayList<>());
    existingHandlers.add(actionHandler);
  }

  public static <T> void doAction(String actionName, T actionData) {
    doAction(actionName, actionData, actionHandlersHostMap);
  }

  public static <T> void doAction(
      String actionName, T actionData, Map<String, List<Handler<?>>> actionHandlersHostMap) {
    List<Handler<?>> actions = actionHandlersHostMap.get(actionName);
    if(actions != null) {
      for(Handler action : actions) {
        action.handle(actionData);
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
