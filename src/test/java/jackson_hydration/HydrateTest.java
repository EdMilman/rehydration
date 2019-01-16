package jackson_hydration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.tools.javac.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;


class HydrateTest {

    private static ObjectMapper mapper;
    private static JsonNode node1;
    private static JsonNode node2;
    private static JsonNode node3;
    private static JsonNode node4;
    private static JsonNode node5;
    private static JsonNode node6;
    private static JsonNode node7;
    private static JsonNode node8;


    @BeforeAll
    public static void setUp() throws IOException {
        mapper = new ObjectMapper();
        node1 = mapper.readTree("{\"name\":\"Steve\"}");
        node2 = mapper.readTree("{\"name\":\"Sarah\"}");
        node3 = mapper.readTree("{\"balance\": \"5\" }");
        node4 = mapper.readTree("{\"balance\": null }");
        node5 = mapper.readTree("{\"name\": \"Phillip\" }");
        node6 = mapper.readTree("{\"balance\": \"10\" }");
        node7 = mapper.readTree("{\"balance\": \"20\" }");
        node8 = mapper.readTree("{\"sausage\": \"chutney\" }");
    }
    @Test
    void accountHydrationTest() throws IOException {
        Collection<Event> events = List.of(
                new Event(node1),
                new Event(node2),
                new Event(node6),
                new Event(node7)

        );
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Account ac = new Account("Sarah", "20");
        assertEquals(ac, Account.hydrateAccount(events));
    }
}