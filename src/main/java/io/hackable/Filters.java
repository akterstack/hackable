package io.hackable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Filters {

    private static final Class<? extends Hackable> GLOBAL_CONTEXT = Hackable.class;

    private static final Map<String, List<Filter<?>>> filterHandlersMap = new HashMap<>();

    private Filters(){
    }

    public static void onFilter(String filterName, Filter<?> filterHandler) {
        onFilter(filterName, GLOBAL_CONTEXT, filterHandler);
    }

    public static void onFilter(String filterName, Class<? extends Hackable> contextClass, Filter<?> filterHandler) {
        String hostKey = resolveFilterHandlerKey(contextClass, filterName);
        List<Filter<?>> existingHandlers = filterHandlersMap.get(hostKey);
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
        List<Filter<?>> filters = filterHandlersMap.get(resolveFilterHandlerKey(clazz, filterName));
        if(filters != null) {
            R fobj = filterableObject;
            for(Filter f : filters) {
                fobj = (R)f.filter(fobj);
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
