import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AccountAggregate {
    private UUID accountId = UUID.randomUUID();

    private AccountHolderAggregate accountHolder;
    private AccountBalanceAggregate accountBalanceAggregate;

    public AccountAggregate(AccountHolderAggregate accountHolder, AccountBalanceAggregate accountBalanceAggregate) {
        this.accountHolder = accountHolder;
        this.accountBalanceAggregate = accountBalanceAggregate;
    }

    private AccountAggregate(HydrationBuilder builder) {
        this.accountHolder = builder.accountHolder;
        this.accountBalanceAggregate = builder.accountBalanceAggregate;
        this.accountId = builder.accountId;
    }

    private AccountAggregate() {
    }

    public void setAccountHolder(AccountHolderAggregate accountHolder) {
        this.accountHolder = accountHolder;
    }

    public void setAccountBalanceAggregate(AccountBalanceAggregate accountBalanceAggregate) {
        this.accountBalanceAggregate = accountBalanceAggregate;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public AccountHolderAggregate getAccountHolder() {
        return accountHolder;
    }

    public AccountBalanceAggregate getAccountBalanceAggregate() {
        return accountBalanceAggregate;
    }

    @Override
    public String toString() {
        return "AccountAggregate{" +
                "accountId=" + accountId +
                ", accountHolder='" + accountHolder + '\'' +
                ", accountBalanceAggregate=" + accountBalanceAggregate +
                '}';
    }

    public static AccountAggregate hydrationBuilder(JsonNode... nodes) {
        return new HydrationBuilder().build(nodes);
    }

    private static class HydrationBuilder {
        private AccountHolderAggregate accountHolder;
        private AccountBalanceAggregate accountBalanceAggregate;
        private UUID accountId;

        public AccountAggregate build(JsonNode... nodes) {
            Arrays.stream(nodes).map(node -> node.get("balance")).reduce((first, second) -> second).get();
            Arrays.stream(nodes).forEach(n -> n.fields().forEachRemaining(node -> {
                        switch (node.getKey()) {
                            case "accountHolder":
                                /*this.accountHolder = node.getValue().textValue();*/
                                break;
                            case "accountBalanceAggregate":
                               /* accountBalanceAggregate = Double.parseDouble(node.getValue().textValue());*/
                                break;
                            case "accountId":
                                this.accountId = UUID.randomUUID();
                                break;
                        }
                    })
            );
            return new AccountAggregate(this);
        }
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"accountId\":\"asdfadf\", \"accountHolder\":\"newname\", \"accountBalanceAggregate\":\"100.0\"}");
        JsonNode node1 = mapper.readTree("{\"accountHolder\":\"other accountHolder\", \"accountBalanceAggregate\":\"50.0\"}");
        JsonNode node2 = mapper.readTree("{\"accountHolder\":\"sam\"}");
        JsonNode node3 = mapper.readTree("{\"accountHolder\":\"tristan\"}");
        JsonNode node4 = mapper.readTree("{\"accountHolder\":\"latest accountHolder\"}");
        JsonNode node5 = mapper.readTree("{\"accountBalanceAggregate\":\"20\"}");
        JsonNode node6 = mapper.readTree("{\"accountBalanceAggregate\":\"10\"}");
        System.out.println(AccountAggregate.hydrationBuilder(node, node1, node2, node3, node4, node5, node6));

    }
}
