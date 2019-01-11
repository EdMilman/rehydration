import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountAggregateFactoryTest {
    private static AccountAggregateFactory factory;
    private static NameChangeEvent updateNameEvent;
    private static NameChangeEvent updateNameEvent2;
    private static BalanceChangeEvent addFundsEvent;
    private static BalanceChangeEvent deductFundsEvent;

    @BeforeAll
    public static void setUp(){
        factory = new AccountAggregateFactory();
        updateNameEvent = new NameChangeEvent("setName", "Lister");
        updateNameEvent.setFunc(x -> updateNameEvent.getItem());
        updateNameEvent2 = new NameChangeEvent("setName", "new setName");
        updateNameEvent2.setFunc(x -> updateNameEvent2.getItem());
        addFundsEvent = new BalanceChangeEvent("setBalance", 100.0);
        addFundsEvent.setFunc(x -> x + addFundsEvent.getItem());
        deductFundsEvent = new BalanceChangeEvent("setBalance", 25.0);
        deductFundsEvent.setFunc(x -> x - deductFundsEvent.getItem());
    }

    @Test
    public void testSetNameChange() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Account hydratedAccount = (Account) factory.hydrate(Account.class, updateNameEvent);
        assertEquals("Lister", hydratedAccount.getName());
    }

    @Test
    public void testTwoNameChanges() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Account hydratedAccount = (Account) factory.hydrate(Account.class, updateNameEvent, updateNameEvent2);
        assertEquals("new setName", hydratedAccount.getName());
    }

    @Test
    public void testAddToBalance() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Account hydratedAccount = (Account) factory.hydrate(Account.class, addFundsEvent);
        assertEquals((Double)100.0, hydratedAccount.getBalance());
    }

    @Test
    public void testAddAndSubtractToBalance() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Account hydratedAccount = (Account) factory.hydrate(Account.class, addFundsEvent, deductFundsEvent);
        assertEquals((Double)75.0, hydratedAccount.getBalance());
    }
}