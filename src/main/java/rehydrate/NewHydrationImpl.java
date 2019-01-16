package rehydrate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import rehydrate.converters.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NewHydrationImpl implements Hydration {
    private final ObjectMapper objectMapper;

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

    public NewHydrationImpl() {
        objectMapper = new ObjectMapper();

    }

    @Override
    public Object applyEvents(Class clazz, Collection<Event> events) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object result = constructor.newInstance();
        Map resultMap = new HashMap();

        for (Event event : events) {
            try {

//                AccountHolderAggregate accountHolderAggregate = objectMapper.readValue(event.getPayload().toString(), AccountHolderAggregate.class);
//
//                result = objectMapper.updateValue(result, accountHolderAggregate);

                Map map = objectMapper.convertValue(event.getPayload(), Map.class);
                resultMap.putAll(map);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(resultMap);

      //  ObjectReader reader = objectMapper.readerForUpdating(result);
//        events.forEach(event -> {
//            AccountHolderAggregate accountHolderAggregate = null;
//            try {
//                accountHolderAggregate = objectMapper.readValue(event.getPayload().toString(), AccountHolderAggregate.class);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            reader.withValueToUpdate(accountHolderAggregate);
//        });

        return result;

    }
}
