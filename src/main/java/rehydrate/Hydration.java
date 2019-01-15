package rehydrate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public interface Hydration {
    default Object applyEvents(Class clazz, Collection<Event> events) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object result = constructor.newInstance();
        ObjectMapper mapper = new ObjectMapper();
        Arrays.stream(result.getClass().getDeclaredFields())
                .forEach(field -> {
                            field.setAccessible(true);
                            events.stream()
                                    .filter(event -> mapper.convertValue(event.getPayload(), Map.class).containsKey(field.getName()))
                                    .map(event -> event.getPayload().get(field.getName()))
                                    .reduce((first, second) -> second)
                                    .ifPresent(val -> {
                                        switch (field.getType().getSimpleName()) {
                                            case "Integer":
                                            case "int":
                                                try {
                                                    field.set(result, Integer.parseInt(val.textValue()));
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "Double":
                                            case "double":
                                                try {
                                                    field.set(result, Double.valueOf(val.textValue()));
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                            case "bigInteger":
                                            case "BigInteger":
                                                try {
                                                    field.set(result, BigInteger.valueOf(Long.valueOf(val.textValue())));
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                            default:
                                                try {
                                                    field.set(result, val.textValue());
                                                } catch (IllegalAccessException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
                                        }
                                    });
                        }
                );
        return result;
    }


}
