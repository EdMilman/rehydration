import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountAggregateAggregateFactoryTest {
    private static AccountAggregateFactory factory;
    private static NameChangeEvent updateNameEvent;
    private static NameChangeEvent updateNameEvent2;
    private static BalanceChangeEvent addFundsEvent;
    private static BalanceChangeEvent deductFundsEvent;

    @BeforeAll
    public static void setUp(){
        factory = new AccountAggregateFactory();
        updateNameEvent = new NameChangeEvent("setAccountHolder", "Lister");
        updateNameEvent.setFunc(x -> updateNameEvent.getItem());
        updateNameEvent2 = new NameChangeEvent("setAccountHolder", "new setAccountHolder");
        updateNameEvent2.setFunc(x -> updateNameEvent2.getItem());
        addFundsEvent = new BalanceChangeEvent("setAccountBalanceAggregate", 100.0);
        addFundsEvent.setFunc(x -> x + addFundsEvent.getItem());
        deductFundsEvent = new BalanceChangeEvent("setAccountBalanceAggregate", 25.0);
        deductFundsEvent.setFunc(x -> x - deductFundsEvent.getItem());
    }

    @Test
    public void testSetNameChange() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AccountAggregate hydratedAccountAggregate = (AccountAggregate) factory.hydrate(AccountAggregate.class, updateNameEvent);
        assertEquals("Lister", hydratedAccountAggregate.getAccountHolder());
    }

    @Test
    public void testTwoNameChanges() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AccountAggregate hydratedAccountAggregate = (AccountAggregate) factory.hydrate(AccountAggregate.class, updateNameEvent, updateNameEvent2);
        assertEquals("new setAccountHolder", hydratedAccountAggregate.getAccountHolder());
    }

    @Test
    public void testAddToBalance() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AccountAggregate hydratedAccountAggregate = (AccountAggregate) factory.hydrate(AccountAggregate.class, addFundsEvent);
        assertEquals((Double)100.0, hydratedAccountAggregate.getAccountBalanceAggregate());
    }

    @Test
    public void testAddAndSubtractToBalance() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        AccountAggregate hydratedAccountAggregate = (AccountAggregate) factory.hydrate(AccountAggregate.class, addFundsEvent, deductFundsEvent);
        assertEquals((Double)75.0, hydratedAccountAggregate.getAccountBalanceAggregate());
    }
}