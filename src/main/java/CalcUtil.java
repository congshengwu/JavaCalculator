import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Stack;

/**
 * Created by 丛 on 2018/2/12 0012.
 */

class CalcUtil {
    private static String formula;
    private static CalcUtil calcUtil = new CalcUtil();

    private CalcUtil() {}

    public static BigDecimal calculate(String formula) {
        CalcUtil.formula = formula;
        calcUtil.handleFormula();
        calcUtil.getSuffix();
        return calcUtil.calculate();
    }

    /**
     * 格式化输入的计算式,数字以“_”表示结束
     */
    private void handleFormula() {
        // 处理空格
        formula = formula.replace(" ", "");
        formula = formula.replace("×", "*");
        formula = formula.replace("÷", "/");
        if (formula.charAt(0) == '-') { // 负号开头
            formula = "0" + formula;
        }
        formula = formula.replace("(-", "(0-"); // 处理负号前是括号
        // 添加数字标记(eg:666.6_),符号前会是数字,结尾会是数字
        formula = formula.replace("+", "_+");
        formula = formula.replace("-", "_-");
        formula = formula.replace("*", "_*");
        formula = formula.replace("/", "_/");
        formula = formula.replace(")", "_)");
        formula += "_";
        // 带括号情况
        if (formula.contains("(") || formula.contains(")")) {
            formula = formula.replace(")_+", ")+");
            formula = formula.replace(")_-", ")-");
            formula = formula.replace(")_*", ")*");
            formula = formula.replace(")_/", ")/");
            formula = formula.replace(")_", ")"); // 结尾是括号
        }
    }

    /**
     * 获取后缀表达式
     */
    private void getSuffix() {
        StringBuilder newFormula = new StringBuilder();
        Stack<Character> stackOperator = new Stack<>();
        for (char c: formula.toCharArray()) {
            if (isNumber(c) || c == '_') { // 数字
                newFormula.append(c);
            } else if (c == '.') { // 小数点
                newFormula.append(c);
            } else if (c == '+' || c == '-') {
                if (stackOperator.empty()) { // 栈为空
                    stackOperator.push(c);
                }
                else {
                    char c1 = stackOperator.peek();
                    while (c1 != '(') { // 是 +、-、*、/ 符号
                        newFormula.append(stackOperator.pop());
                        if (stackOperator.empty())
                            break;
                        c1 = stackOperator.peek();
                    }
                    stackOperator.push(c);
                }
            } else if (c == '*' || c == '/') {
                if (stackOperator.empty()) {
                    stackOperator.push(c);
                }
                else {
                    char c1 = stackOperator.peek();
                    while (c1 == '*' || c1 == '/') {
                        newFormula.append(stackOperator.pop());
                        if (stackOperator.empty())
                            break;
                        c1 = stackOperator.peek();
                    }
                    stackOperator.push(c);
                }
            } else if (c == '(') {
                stackOperator.push(c);
            } else if (c == ')') { // 不需形成数字添加_,因为 ) 前肯定是数字,后肯定是运算符,形成数字交给后面的运算符处理
                char c1 = stackOperator.peek();
                while (c1 != '(') {
                    newFormula.append(stackOperator.pop());
                    if (stackOperator.empty())
                        break;
                    c1 = stackOperator.peek();
                }
                stackOperator.pop(); // 弹出“(”
            }
        }
        while (!stackOperator.empty())
            newFormula.append(stackOperator.pop());
        formula = newFormula.toString();
    }

    private BigDecimal calculate() {
        Stack<BigDecimal> stackNumber = new Stack<>();
        Stack<Character> stackSingleNumber = new Stack<>();
        int intBit = 1; // 整数部分位数
        int dotBit = 0; // 小数部分位数
        for (char c: formula.toCharArray()) {
            if (isNumber(c) || c == '.') {
                stackSingleNumber.push(c);
            } else if (c == '_') {
                BigDecimal b = new BigDecimal(String.valueOf(stackSingleNumber.pop() - 48)); // char转int

                while (!stackSingleNumber.empty()) {
                    char c1 = stackSingleNumber.pop();
                    if (c1 != '.') {                // char转int
                        b = b.add(new BigDecimal(String.valueOf(c1 - 48)).multiply(new BigDecimal("10").pow(intBit)));
                        intBit++;
                    } else {
                        dotBit = intBit;
                    }
                }
                if (dotBit != 0)
                    b = b.multiply(new BigDecimal("0.1").pow(dotBit));
                stackNumber.push(b);
                // 初始化位数变量
                intBit = 1;
                dotBit = 0;
            } else if (c == '+') {
                BigDecimal b1 = stackNumber.pop();
                BigDecimal b2 = stackNumber.pop();
                stackNumber.push(b2.add(b1));
            } else if (c == '-') {
                BigDecimal b1 = stackNumber.pop();
                BigDecimal b2 = stackNumber.pop();
                stackNumber.push(b2.subtract(b1));
            } else if (c == '*') {
                BigDecimal b1 = stackNumber.pop();
                BigDecimal b2 = stackNumber.pop();
                stackNumber.push(b2.multiply(b1));
            } else if (c == '/') {
                BigDecimal b1 = stackNumber.pop();
                BigDecimal b2 = stackNumber.pop();
                stackNumber.push(b2.divide(b1, MathContext.DECIMAL128));
            }
        }
        return stackNumber.pop();
    }

    private boolean isNumber(char c) {
        return (c >= 48 && c <= 57);
    }

}
