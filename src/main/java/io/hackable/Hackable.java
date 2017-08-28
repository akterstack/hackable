package io.hackable;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.hackable.HackEngine.*;
import static io.hackable.Hackable.Context.*;

public interface Hackable<K> {

  enum Context {BEFORE, AFTER}

  default <P> void _before(K actionName, Handler<P> actionHandler) {
    registerActionHandler(resolveActionHostKey(BEFORE, actionName), actionHandler, getActionHandlerHostMap());
  }

  default <P> void _after(K actionName, Handler<P> actionHandler) {
    registerActionHandler(resolveActionHostKey(AFTER, actionName), actionHandler, getActionHandlerHostMap());
  }

  default <P> void _on(Context beforeOrAfter, K actionName, Handler<P> actionHandler) {
    registerActionHandler(resolveActionHostKey(beforeOrAfter, actionName), actionHandler, getActionHandlerHostMap());
  }

  /* default context for '_on' is 'AFTER' */
  default <P> void _on(K actionName, Handler<P> actionHandler) {
    registerActionHandler(resolveActionHostKey(AFTER, resolveHostName(actionName)), actionHandler, getActionHandlerHostMap());
  }

  default <P> void _trigger(Context context, K actionName, P actionData) {
    doAction(resolveActionHostKey(context, resolveHostName(actionName)), actionData, getActionHandlerHostMap());
  }

  default <P> void _trigger(K actionName, P actionData) {
    doAction(resolveActionHostKey(AFTER, resolveHostName(actionName)), actionData, getActionHandlerHostMap());
  }

  default <P> void _trigger(K actionName, P actionData, Consumer<P> consumer) {
    doAction(resolveActionHostKey(BEFORE, resolveHostName(actionName)), actionData, getActionHandlerHostMap());
    consumer.accept(actionData);
    doAction(resolveActionHostKey(AFTER, resolveHostName(actionName)), actionData, getActionHandlerHostMap());
  }

  default <P, R> R _trigger(K actionName, P actionData, Function<P, R> function) {
    doAction(resolveActionHostKey(BEFORE, resolveHostName(actionName)), actionData, getActionHandlerHostMap());
    R result = function.apply(actionData);
    doAction(resolveActionHostKey(AFTER, resolveHostName(actionName)), actionData, getActionHandlerHostMap());
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

  Map<String, List<Handler<?>>> getActionHandlerHostMap();

  Map<String, List<Filter<?>>> getFilterHandlerHostMap();

  /* --------------------------- Static Methods --------------------------------- */

  static <P> void before(Object actionName, Handler<P> actionHandler) {
    registerActionHandler(resolveActionHostKey(BEFORE, actionName), actionHandler);
  }

  static <P> void after(Object actionName, Handler<P> actionHandler) {
    registerActionHandler(resolveActionHostKey(AFTER, actionName), actionHandler);
  }

  static <P> void on(Context beforeOrAfter, Object actionName, Handler<P> actionHandler) {
    registerActionHandler(resolveActionHostKey(beforeOrAfter, actionHandler), actionHandler);
  }

  /* default context for 'on' is 'AFTER' */
  static <P> void on(Object actionName, Handler<P> actionHandler) {
    registerActionHandler(resolveActionHostKey(AFTER, actionHandler), actionHandler);
  }

  static <P> void trigger(Context context, Object actionName, P actionData) {
    doAction(resolveActionHostKey(context, actionName), actionData);
  }

  static <P> void trigger(Object actionName, P actionData) {
    trigger(Context.AFTER, actionName, actionData);
  }

  static <P> void trigger(Object actionName, P actionData, Consumer<P> consumer) {
    trigger(BEFORE, actionName, actionData);
    consumer.accept(actionData);
    trigger(AFTER, actionName, actionData);
  }

  static <P, R> R trigger(Object actionName, P actionData, Function<P, R> function) {
    trigger(BEFORE, actionName, actionData);
    R result = function.apply(actionData);
    trigger(AFTER, actionName, result);
    return result;
  }

  static <R> void onFilter(Object filterName, Filter<R> filterHandler) {
    registerFilterHandler(resolveFilterHostKey(filterName), filterHandler);
  }

  static <R> R filter(String filterName, R filterableObject) {
    return applyFilter(resolveFilterHostKey(filterName), filterableObject);
  }

  static String resolveActionHostKey(Context context, Object hostKey) {
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
