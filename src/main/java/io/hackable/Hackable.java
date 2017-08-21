package io.hackable;

import java.util.function.Supplier;

import static io.hackable.HackEngine.*;
import static io.hackable.HackEngine.Context.*;

public interface Hackable {

  default <T> void _before(String actionName, Handler<T> actionHandler) {
    before(resolveHostKeyName(actionName), actionHandler);
  }

  default <T> void _after(String actionName, Handler<T> actionHandler) {
    after(resolveHostKeyName(actionName), actionHandler);
  }

  default <T> void _on(String actionName, Handler<T> actionHandler) {
    _after(actionName, actionHandler);
  }

  default <T> void _trigger(Context context, String actionName, T actionData) {
    trigger(context, resolveHostKeyName(actionName), actionData);
  }

  default <T> void _trigger(String actionName, T actionData) {
    trigger(resolveHostKeyName(actionName), actionData);
  }

  default <R> void _onFilter(String filterName, Filter<R> filterHandler) {
    registerFilterHandler(resolveHostKeyName(filterName), filterHandler);
  }

  default <R> R _filter(String filterName, R filterableObject) {
    return applyFilter(resolveHostKeyName(filterName), filterableObject);
  }

  default <T> T _wrap(String actionName, T actionData, Supplier<T> supplier) {
    _trigger(BEFORE, actionName, actionData);
    T result = supplier.get();
    _trigger(AFTER, actionName, result);
    return result;
  }

  default String resolveHostKeyName(String name) {
    return name + getClass().getSimpleName();
  }

  static <T> void before(String actionName, Handler<T> actionHandler) {
    registerActionHandler(BEFORE, actionName, actionHandler);
  }

  static <T> void after(String actionName, Handler<T> actionHandler) {
    registerActionHandler(AFTER, actionName, actionHandler);
  }

  /* alias method of after() */
  static <T> void on(String actionName, Handler<T> actionHandler) {
    after(actionName, actionHandler);
  }

  static <T> void trigger(Context context, String actionName, T actionData) {
    doAction(context, actionName, actionData);
  }

  static <T> void trigger(String actionName, T actionData) {
    trigger(Context.AFTER, actionName, actionData);
  }

  static <R> void onFilter(String filterName, Filter<R> filterHandler) {
    registerFilterHandler(filterName, filterHandler);
  }

  static <R> R filter(String filterName, R filterableObject) {
    return applyFilter(filterName, filterableObject);
  }

  static <T> T wrap(String actionName, T actionData, Supplier<T> supplier) {
    trigger(BEFORE, actionName, actionData);
    T result = supplier.get();
    trigger(AFTER, actionName, result);
    return result;
  }

}
