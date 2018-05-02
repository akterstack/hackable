package hackable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static hackable.HackEngine.*;
import static hackable.Hackable.Context.*;

public interface Hackable<K> {

  enum Context {BEFORE, AFTER}

  default <P> void _before(K eventName, EventHandler<P> eventEventHandler) {
    registerEventHandler(resolveEventHostKey(BEFORE, eventName), eventEventHandler, getEventHandlerHostMap());
  }

  default <P> void _after(K eventName, EventHandler<P> eventEventHandler) {
    registerEventHandler(resolveEventHostKey(AFTER, eventName), eventEventHandler, getEventHandlerHostMap());
  }

  default <P> void _on(Context beforeOrAfter, K eventName, EventHandler<P> eventEventHandler) {
    registerEventHandler(resolveEventHostKey(beforeOrAfter, eventName), eventEventHandler, getEventHandlerHostMap());
  }

  /* default context for '_on' is 'AFTER' */
  default <P> void _on(K eventName, EventHandler<P> eventEventHandler) {
    registerEventHandler(resolveEventHostKey(AFTER, resolveHostName(eventName)), eventEventHandler, getEventHandlerHostMap());
  }

  default <P> void _trigger(Context context, K eventName, P eventData) {
    doEvent(resolveEventHostKey(context, resolveHostName(eventName)), eventData, getEventHandlerHostMap());
  }

  default <P> void _trigger(K eventName, P eventData) {
    doEvent(resolveEventHostKey(AFTER, resolveHostName(eventName)), eventData, getEventHandlerHostMap());
  }

  default <P> void _trigger(K eventName, P eventData, Consumer<P> consumer) {
    doEvent(resolveEventHostKey(BEFORE, resolveHostName(eventName)), eventData, getEventHandlerHostMap());
    consumer.accept(eventData);
    doEvent(resolveEventHostKey(AFTER, resolveHostName(eventName)), eventData, getEventHandlerHostMap());
  }

  default <P, R> R _trigger(K eventName, P eventData, Function<P, R> function) {
    doEvent(resolveEventHostKey(BEFORE, resolveHostName(eventName)), eventData, getEventHandlerHostMap());
    R result = function.apply(eventData);
    doEvent(resolveEventHostKey(AFTER, resolveHostName(eventName)), eventData, getEventHandlerHostMap());
    return result;
  }

  default <R> void _onFilter(K filterName, Filter<R> filterHandler) {
    registerFilterHandler(resolveFilterHostKey(resolveHostName(filterName)), filterHandler, getFilterHandlerHostMap());
  }

  default <R> R _filter(K filterName, R filterableObject) {
    return applyFilter(resolveFilterHostKey(resolveHostName(filterName)), filterableObject, getFilterHandlerHostMap());
  }

  default String resolveHostName(K key) {
    return hostKeyToString(key) + ":" + getHackableClassName();
  }

  default String hostKeyToString(K hostKey) {
    return hostKey.toString();
  }

  default String getHackableClassName() {
    return getClass().getName();
  }

  Map<String, List<EventHandler<?>>> getEventHandlerHostMap();

  Map<String, List<Filter<?>>> getFilterHandlerHostMap();

  /* --------------------------- Static Methods --------------------------------- */

  static <P> void before(Object eventName, EventHandler<P> eventEventHandler) {
    registerEventHandler(resolveEventHostKey(BEFORE, eventName), eventEventHandler);
  }

  static <P> void after(Object eventName, EventHandler<P> eventEventHandler) {
    registerEventHandler(resolveEventHostKey(AFTER, eventName), eventEventHandler);
  }

  static <P> void on(Context beforeOrAfter, Object eventName, EventHandler<P> eventEventHandler) {
    registerEventHandler(resolveEventHostKey(beforeOrAfter, eventEventHandler), eventEventHandler);
  }

  /* default context for 'on' is 'AFTER' */
  static <P> void on(Object eventName, EventHandler<P> eventEventHandler) {
    registerEventHandler(resolveEventHostKey(AFTER, eventEventHandler), eventEventHandler);
  }

  static <P> void trigger(Context context, Object eventName, P eventData) {
    doEvent(resolveEventHostKey(context, eventName), eventData);
  }

  static <P> void trigger(Object eventName, P eventData) {
    trigger(Context.AFTER, eventName, eventData);
  }

  static <P> void trigger(Object eventName, P eventData, Consumer<P> consumer) {
    trigger(BEFORE, eventName, eventData);
    consumer.accept(eventData);
    trigger(AFTER, eventName, eventData);
  }

  static <P, R> R trigger(Object eventName, P eventData, Function<P, R> function) {
    trigger(BEFORE, eventName, eventData);
    R result = function.apply(eventData);
    trigger(AFTER, eventName, result);
    return result;
  }

  static <R> void onFilter(Object filterName, Filter<R> filterHandler) {
    registerFilterHandler(resolveFilterHostKey(filterName), filterHandler);
  }

  static <R> R filter(String filterName, R filterableObject) {
    return applyFilter(resolveFilterHostKey(filterName), filterableObject);
  }

  static String resolveEventHostKey(Context context, Object hostKey) {
    return context.name().toLowerCase() + ":" + hostKey.toString().toLowerCase();
  }

  static String resolveFilterHostKey(Object hostKey) {
    return hostKey.toString().toLowerCase();
  }

  static Hackable forClass(Class<? extends Hackable> hackableClass)
      throws IllegalAccessException, InstantiationException {
    return hackableClass.newInstance();
  }

}
