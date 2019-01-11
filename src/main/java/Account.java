import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

public class Account {
    private String name;
    private double balance;
    private UUID accountId = UUID.randomUUID();

    public Account(String name, Double balance) {
        this.name = name;
        this.balance = balance;
    }

    private Account(HydrationBuilder builder) {
        this.name = builder.name;
        this.balance = builder.balance;
        this.accountId = builder.accountId;
    }

    private Account() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public Double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                '}';
    }

    public static HydrationBuilder hydrationBuilder() {
        return new HydrationBuilder();
    }

    private static class HydrationBuilder {
        private String name;
        private Double balance;
        private UUID accountId;

        public Account build(JsonNode... nodes) {
            for (JsonNode n : nodes) {
                n.fields().forEachRemaining(node -> {
                    switch (node.getKey()) {
                        case "name":
                            this.name = node.getValue().textValue();
                            break;
                        case "balance":
                            if(this.balance != null){
                                this.balance += Double.parseDouble(node.getValue().textValue());
                            } else {
                                this.balance = Double.parseDouble(node.getValue().textValue());
                            }
                            break;
                        case "accountId":
                            this.accountId = UUID.randomUUID();
                            break;
                    }
                });
            }
            return new Account(this);
        }
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"accountId\":\"asdfadf\", \"name\":\"newname\", \"balance\":\"100.0\"}");
        JsonNode node1 = mapper.readTree("{\"name\":\"other name\", \"balance\":\"-50.0\"}");
        System.out.println(Account.hydrationBuilder().build(node, node1));
    }
}
