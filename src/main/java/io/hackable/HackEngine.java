package io.hackable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public final class HackEngine {

  public enum Context {BEFORE, AFTER}

  private static final Map<String, List<Handler<?>>> actionHandlersHostMap = new HashMap<>();
  private static final Map<String, List<Filter<?>>> filterHandlersHostMap = new HashMap<>();

  private HackEngine() {}

  public static <T> void registerActionHandler(Context context, String actionName, Handler<T> actionHandler) {
    List<Handler<?>> existingHandlers =
        actionHandlersHostMap
            .computeIfAbsent(resolveActionHostKey(context, actionName), k -> new ArrayList<>());
    existingHandlers.add(actionHandler);
  }

  public static <T> void doAction(Context context, String actionName, T actionData) {
    List<Handler<?>> actions = actionHandlersHostMap.get(resolveActionHostKey(context, actionName));
    if(actions != null) {
      for(Handler action : actions) {
        action.handle(actionData);
      }
    }
  }

  public static <R> void registerFilterHandler(String filterName, Filter<R> filterHandler) {
    String filterHostKey = filterName.toLowerCase();
    List<Filter<?>> existingHandlers =
        filterHandlersHostMap.computeIfAbsent(filterHostKey, k -> new LinkedList<>());
    existingHandlers.add(filterHandler);
  }

  public static <R> R applyFilter(String filterName, R filterableObject) {
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

  private static String resolveActionHostKey(Context context, String actionName) {
    return context.name().toLowerCase() + ":" + actionName.toLowerCase();
  }

}
