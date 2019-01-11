import java.util.function.Function;

public class NameChangeEvent implements Event<String> {
    private String fieldName;
    private String item;
    private Function<String, String> func;

    public NameChangeEvent(String fieldName, String item) {
        this.item = item;
        this.fieldName = fieldName;
    }

    public void setFunc(Function<String, String> func) {
        this.func = func;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Function<String, String> getFunc() {
        return func;
    }

    @Override
    public String getItem() {
        return item;
    }
}
