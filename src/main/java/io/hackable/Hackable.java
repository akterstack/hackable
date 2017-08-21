package io.hackable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class Hackable {

  private enum Context {BEFORE, AFTER}

  private static final Map<String, List<Handler<?>>> actionHandlersHostMap = new HashMap<>();
  private static final Map<String, List<Filter<?>>> filterHandlersHostMap = new HashMap<>();

  private Hackable() {}

  public static <T> void before(String actionName, Handler<T> actionHandler) {
    registerActionHandler(Context.BEFORE, actionName, actionHandler);
  }

  public static <T> void after(String actionName, Handler<T> actionHandler) {
    registerActionHandler(Context.AFTER, actionName, actionHandler);
  }

  /* alias method of after() */
  public static <T> void on(String actionName, Handler<T> actionHandler) {
    after(actionName, actionHandler);
  }

  private static <T> void registerActionHandler(Context context, String actionName, Handler<T> actionHandler) {
    List<Handler<?>> existingHandlers =
        actionHandlersHostMap
            .computeIfAbsent(resolveActionHostKey(context, actionName), k -> new ArrayList<>());
    existingHandlers.add(actionHandler);
  }

  public static <T> void trigger(String actionName, T actionData) {
    trigger(Context.AFTER, actionName, actionData);
  }

  public static <T> void trigger(Context context, String actionName, T actionData) {
    doAction(context, actionName, actionData);
  }

  private static <T> void doAction(Context context, String actionName, T actionData) {
    List<Handler<?>> actions = actionHandlersHostMap.get(resolveActionHostKey(context, actionName));
    if(actions != null) {
      for(Handler action : actions) {
        action.handle(actionData);
      }
    }
  }

  public static <R> void onFilter(String filterName, Filter<R> filterHandler) {
    registerFilterHandler(filterName, filterHandler);
  }

  private static <R> void registerFilterHandler(String filterName, Filter<R> filterHandler) {
    String filterHostKey = filterName.toLowerCase();
    List<Filter<?>> existingHandlers =
        filterHandlersHostMap.computeIfAbsent(filterHostKey, k -> new LinkedList<>());
    existingHandlers.add(filterHandler);
  }

  public static <R> R filter(String filterName, R filterableObject) {
    return applyFilter(filterName, filterableObject);
  }

  private static <R> R applyFilter(String filterName, R filterableObject) {
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

  public static <T> T hack(String actionName, T actionData, Supplier<T> supplier) {
    Hackable.trigger(Context.BEFORE, actionName, actionData);
    T result = supplier.get();
    Hackable.trigger(Context.AFTER, actionName, result);
    return result;
  }

}
