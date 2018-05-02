package hackable;

@FunctionalInterface
public interface EventHandler<T> {

    void handle(T t);

}
