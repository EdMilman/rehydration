import java.util.function.Function;

public interface Event<T> {
    String getFieldName();
    Function<T, T> getFunc();
    T getItem();
}
