package io.hackable;

import java.util.function.Consumer;
import java.util.function.Function;

import static io.hackable.HackEngine.*;
import static io.hackable.HackEngine.Context.*;

public interface Hackable<K> {

  default <H> void _before(K actionName, Handler<H> actionHandler) {
    before(resolveHostName(actionName), actionHandler);
  }

  default <H> void _after(K actionName, Handler<H> actionHandler) {
    after(resolveHostName(actionName), actionHandler);
  }

  /* alias method of _after() */
  default <H> void _on(K actionName, Handler<H> actionHandler) {
    _after(actionName, actionHandler);
  }

  default <H> void _trigger(Context context, K actionName, H actionData) {
    trigger(context, resolveHostName(actionName), actionData);
  }

  default <H> void _trigger(K actionName, H actionData) {
    trigger(resolveHostName(actionName), actionData);
  }

  default <H> void _trigger(K actionName, H actionData, Consumer<H> consumer) {
    trigger(resolveHostName(actionName), actionData, consumer);
  }

  default <H, R> R _trigger(K actionName, H actionData, Function<H, R> function) {
    return trigger(resolveHostName(actionName), actionData, function);
  }

  default <R> void _onFilter(K filterName, Filter<R> filterHandler) {
    registerFilterHandler(resolveHostName(filterName), filterHandler);
  }

  default <R> R _filter(K filterName, R filterableObject) {
    return applyFilter(resolveHostName(filterName), filterableObject);
  }

  default String resolveHostName(K key) {
    return hostKeyToString(key) + ":" + getHackableClassName();
  }

  default String hostKeyToString(K hostKey) {
    return hostKey.toString();
  }

  default String getHackableClassName() {
    return getClass().getSimpleName();
  }

  /* --------------------------- Static Methods --------------------------------- */

  static <K, H> void before(K actionName, Handler<H> actionHandler) {
    before(actionName.toString(), actionHandler);
  }

  static <H> void before(Enum actionName, Handler<H> actionHandler) {
    before(actionName.toString(), actionHandler);
  }

  static <H> void before(String actionName, Handler<H> actionHandler) {
    registerActionHandler(BEFORE, actionName, actionHandler);
  }

  static <K, H> void after(K actionName, Handler<H> actionHandler) {
    after(actionName.toString(), actionHandler);
  }

  static <H> void after(Enum actionName, Handler<H> actionHandler) {
    after(actionName.toString(), actionHandler);
  }

  static <H> void after(String actionName, Handler<H> actionHandler) {
    registerActionHandler(AFTER, actionName, actionHandler);
  }

  /* alias method of after(K, Handler) */
  static <K, H> void on(K actionName, Handler<H> actionHandler) {
    after(actionName, actionHandler);
  }

  /* alias method of after(Enum, Handler) */
  static <H> void on(Enum actionName, Handler<H> actionHandler) {
    after(actionName, actionHandler);
  }

  /* alias method of after(String, Handler) */
  static <H> void on(String actionName, Handler<H> actionHandler) {
    after(actionName, actionHandler);
  }

  static <K, H> void trigger(Context context, K actionName, H actionData) {
    trigger(context, actionName.toString(), actionData);
  }

  static <H> void trigger(Context context, Enum actionName, H actionData) {
    doAction(context, actionName.toString(), actionData);
  }

  static <H> void trigger(Context context, String actionName, H actionData) {
    doAction(context, actionName, actionData);
  }

  static <K, P> void trigger(K actionName, P actionData) {
    trigger(actionName.toString(), actionData);
  }

  static <P> void trigger(Enum actionName, P actionData) {
    trigger(actionName.toString(), actionData);
  }

  static <P> void trigger(String actionName, P actionData) {
    trigger(Context.AFTER, actionName, actionData);
  }

  static <K, P> void trigger(K actionName, P actionData, Consumer<P> consumer) {
    trigger(actionName.toString(), actionData, consumer);
  }

  static <P> void trigger(Enum actionName, P actionData, Consumer<P> consumer) {
    trigger(actionName.toString(), actionData, consumer);
  }

  static <P> void trigger(String actionName, P actionData, Consumer<P> consumer) {
    trigger(BEFORE, actionName, actionData);
    consumer.accept(actionData);
    trigger(AFTER, actionName, actionData);
  }

  static <K, P, R> R trigger(K actionName, P actionData, Function<P, R> function) {
    return trigger(actionName.toString(), actionData, function);
  }

  static <P, R> R trigger(Enum actionName, P actionData, Function<P, R> function) {
    return trigger(actionName.toString(), actionData, function);
  }

  static <P, R> R trigger(String actionName, P actionData, Function<P, R> function) {
    trigger(BEFORE, actionName, actionData);
    R result = function.apply(actionData);
    trigger(AFTER, actionName, result);
    return result;
  }

  static <K, R> void onFilter(K filterName, Filter<R> filterHandler) {
    onFilter(filterName.toString(), filterHandler);
  }

  static <R> void onFilter(Enum filterName, Filter<R> filterHandler) {
    onFilter(filterName.toString(), filterHandler);
  }

  static <R> void onFilter(String filterName, Filter<R> filterHandler) {
    registerFilterHandler(filterName, filterHandler);
  }

  static <K, R> R filter(K filterName, R filterableObject) {
    return filter(filterName.toString(), filterableObject);
  }

  static <R> R filter(Enum filterName, R filterableObject) {
    return filter(filterName.toString(), filterableObject);
  }

  static <R> R filter(String filterName, R filterableObject) {
    return applyFilter(filterName, filterableObject);
  }

}
