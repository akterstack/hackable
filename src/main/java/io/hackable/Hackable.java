package io.hackable;

import java.util.function.Consumer;
import java.util.function.Function;

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

  default <T> T _trigger(String actionName, T actionData, Consumer<T> consumer) {
    return trigger(resolveHostKeyName(actionName), actionData, consumer);
  }

  default <T, R> R _trigger(String actionName, T actionData, Function<T, R> function) {
    return trigger(resolveHostKeyName(actionName), actionData, function);
  }

  default <R> void _onFilter(String filterName, Filter<R> filterHandler) {
    registerFilterHandler(resolveHostKeyName(filterName), filterHandler);
  }

  default <R> R _filter(String filterName, R filterableObject) {
    return applyFilter(resolveHostKeyName(filterName), filterableObject);
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

  static <T> T trigger(String actionName, T actionData, Consumer<T> consumer) {
    trigger(BEFORE, actionName, actionData);
    consumer.accept(actionData);
    trigger(AFTER, actionName, actionData);
    return actionData;
  }

  static <T, R> R trigger(String actionName, T actionData, Function<T, R> function) {
    trigger(BEFORE, actionName, actionData);
    R result = function.apply(actionData);
    trigger(AFTER, actionName, result);
    return result;
  }

  static <R> void onFilter(String filterName, Filter<R> filterHandler) {
    registerFilterHandler(filterName, filterHandler);
  }

  static <R> R filter(String filterName, R filterableObject) {
    return applyFilter(filterName, filterableObject);
  }

}
