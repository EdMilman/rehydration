import java.util.function.Function;

public class BalanceChangeEvent implements Event<Double> {
    private String fieldName;
    private Double item;
    private Function<Double, Double> func;

    public BalanceChangeEvent(String fieldName, Double item) {
        this.fieldName = fieldName;
        this.item = item;
    }

    public void setFunc(Function<Double, Double> func) {
        this.func = func;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Function<Double, Double> getFunc() {
        return func;
    }

    @Override
    public Double getItem() {
        return item;
    }
}
