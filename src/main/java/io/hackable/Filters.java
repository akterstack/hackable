package io.hackable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Filters {

    private static final Class<? extends Hackable> GLOBAL_CONTEXT = Hackable.class;

    private static final Map<String, List<Supplier<?>>> filterHandlersMap = new HashMap<>();

    private Filters(){
    }

    public static <R> void onFilter(String filterName, Supplier<R> filterHandler) {
        applyFilter(filterName, GLOBAL_CONTEXT, filterHandler);
    }

    public static <R> void onFilter(String filterName, Class<? extends Hackable> contextClass, Supplier<R> filterHandler) {
        String hostKey = resolveFilterHandlerKey(contextClass, filterName);
        List<Supplier<?>> existingHandlers = filterHandlersMap.get(hostKey);
        if(existingHandlers == null) {
            existingHandlers = new LinkedList<>();
            filterHandlersMap.put(hostKey, existingHandlers);
        }
        existingHandlers.add(filterHandler);
    }

    public static <R> R applyFilter(String filterName, R filterableObject) {
        return applyFilter(filterName, GLOBAL_CONTEXT, filterableObject);
    }

    public static <R> R applyFilter(String filterName, Class<? extends Hackable> clazz, R filterableObject) {
        Filter filter = new Filter(filterName, filterableObject);
        List<Supplier<?>> suppliers = filterHandlersMap.get(resolveFilterHandlerKey(clazz, filterName));
        if(suppliers != null) {
            R fobj = filterableObject;
            for(Supplier<?> supplier : suppliers) {
                fobj = (R)supplier.get();
            }
            return fobj;
        }
        return filterableObject;
    }

    private static String resolveFilterHandlerKey(Class<? extends Hackable> contextClass, String filterName) {
        if(contextClass == null) contextClass = GLOBAL_CONTEXT;
        return contextClass.getCanonicalName() + ":" + filterName;
    }

}
