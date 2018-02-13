import java.math.BigDecimal;

/**
 * Created by 丛 on 2018/2/12 0012.
 */

public class Calculator {
    private String formula;

    public static Calculator input(String formula) {
        return new Calculator(formula);
    }

    private Calculator(String formula) {
        this.formula = formula;
    }

    public BigDecimal getResult() {
        return CalcUtil.calculate(formula);
    }

}
