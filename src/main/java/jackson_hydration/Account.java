package jackson_hydration;


import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

public class Account {

    private String name;
    private String balance;

    public Account() {
    }

    private Account(JsonNode node){
        this.name = node.get("name").textValue();
        this.balance = node.get("balance").textValue();
    }

    public Account(String name, String balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(name, account.name) &&
                Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, balance);
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }


    public static Account hydrateAccount(Collection<Event> events) throws IOException {
        return new Hydration(events).apply();
    }

    private static class Hydration implements Hydrate{
        private Collection<Event> events;

        private Hydration (Collection<Event> events){
            this.events = events;
        }

        public Account apply() throws IOException {
            return new Account(hydrate(this.events));
        }

    }

}
