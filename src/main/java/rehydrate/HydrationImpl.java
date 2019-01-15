package rehydrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import rehydrate.converters.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class HydrationImpl implements Hydration {

    private final Map<String, FieldSetter> fieldSetterMap = new HashMap<String, FieldSetter>(){
        {
            put("Integer", new IntegerFieldSetter());
            put("int", new IntegerFieldSetter());
            put("Double", new DoubleFieldSetter());
            put("String", new StringFieldSetter());
            put("Address", new AddressFieldSetter());
            put("address", new AddressFieldSetter());
        }
    };

    @Override
    public Object applyEvents(Class clazz, Collection<Event> events) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
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
                                        FieldSetter fieldSetter = fieldSetterMap.getOrDefault(field.getType().getSimpleName(), new StringFieldSetter() );
                                        try {
                                            fieldSetter.setField(result, field, val);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    });
                        }
                );
        return result;
    }
}
