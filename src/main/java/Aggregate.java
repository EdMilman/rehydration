import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Function;

public interface Aggregate<T> {
    default Object hydrate(Class clazz, Event... events) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = clazz.getDeclaredConstructor();
        // TODO: 2019-01-09 not all classes will have no args constructor - throw specific exception ?
        constructor.setAccessible(true);
        T result = (T) constructor.newInstance();
        for (Field field : result.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (Arrays.stream(events).filter(event -> event.getFieldName() == (field.getName())).count() > 0) {
                field.set(result, Arrays.stream(events)
                        .filter(event -> event.getFieldName() == field.getName())
                        .map(event -> event.getFunc())
                        .reduce(Function.identity(),
                                Function::andThen)
                        .apply(field.get(result)));
            }
        }
        return result;
    }
}
