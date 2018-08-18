package com.csw.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Stack;

/**
 * 说明：
 *      1.用到了BigDecimal类,它的精度范围比较高,确保计算结果的准确性。
 *      2.关于处理负号,如-5+2,会变为0-5+2,在负号前添0。
 *      3.Main类中有演示使用方法
 * Created by 丛 on 2018/2/12 0012.
 */

public class Calculator {
    private static String formula; // 输入的计算式

    private Calculator() {}

    public static Calculator input(String inputFormula) {
        formula = inputFormula;
        return new Calculator();
    }

    /**
     * 获取结果,带有指定保留位数功能
     * @param accurate 小数点后保留的位数
     * @return 经过小数保留后的字符串结果
     */
    public String getResult(int accurate) {
        if (accurate >= 0)
            return getRawResult().setScale(accurate, RoundingMode.HALF_UP).toPlainString();
        else
            return getResult();
    }

    /**
     * 获取结果
     * @return 获取字符串形式的结果
     */
    public String getResult() {
        return getRawResult().stripTrailingZeros().toPlainString();
    }

    /**
     * 获取BigDecimal结果方法
     * @return 以BigDecimal的形式返回结果
     */
    public BigDecimal getRawResult() {
        handleFormula(); // 将字符串计算式中的空格处理掉,为生成后缀表达式做一些处理。
        getSuffixFormula(); // 生成后缀表达式计算公式字符串,便于程序处理。
        return calculate(); // 返回计算结果
    }

    /**
     * 将输入计算式中的每个数字以“_”结尾,表示一个数字结束,方便得到后缀表达式以及计算结果
     */
    private void handleFormula() {
        formula = formula.replace(" ", ""); // 处理计算式中的空格
        formula = formula.replace("×", "*"); // 将×替换为*用于计算
        formula = formula.replace("÷", "/"); // 将÷替换为/用于计算
        if (formula.charAt(0) == '-') { // 计算式以负号开头,在前面加0
            formula = "0" + formula;
        }
        // 处理负号前面是括号的情况
        formula = formula.replace("(-", "(0-");
        // 下面5种运算符前可能是数字,在前面加下划线"_"
        formula = formula.replace("+", "_+");
        formula = formula.replace("-", "_-");
        formula = formula.replace("*", "_*");
        formula = formula.replace("/", "_/");
        formula = formula.replace(")", "_)");
        formula += "_"; // 计算式结尾可能是数字,结尾后加"下划线_"
        // 计算式如果带括号,上面加的下划线可能没有加在数字后面,纠正一下
        if (formula.contains("(") || formula.contains(")")) {
            formula = formula.replace(")_+", ")+"); // 加号的左面是括号
            formula = formula.replace(")_-", ")-"); // 减号的左面是括号
            formula = formula.replace(")_*", ")*"); // 乘号的左面是括号
            formula = formula.replace(")_/", ")/"); // 除号的左面是括号
            formula = formula.replace(")_", ")"); // 结尾是括号
        }
    }

    /**
     * 获取后缀表达式,后缀表达字符串中会保留handleFormula()中的下划线
     * Stack是Java提供的一个实现栈效果的类,stack.push(xxx)是入栈,stack.pop(xxx)是出栈,stack.peek()是查看栈顶元素
     */
    private void getSuffixFormula() {
        StringBuilder suffixFormula = new StringBuilder(); // 后缀表达式字符串
        Stack<Character> stackOperator = new Stack<>();
        for (char c: formula.toCharArray()) { // 将计算式字符串以char型的方式遍历一遍
            if (isNumber(c) || c == '_') { // 当前char为数字或下划线
                suffixFormula.append(c);
            } else if (c == '.') { // 小数点
                suffixFormula.append(c);
            } else if (c == '+' || c == '-') {
                if (stackOperator.empty()) { // 栈为空
                    stackOperator.push(c);
                }
                else {
                    char c1 = stackOperator.peek();
                    while (c1 != '(') { // 是 +、-、*、/ 符号
                        suffixFormula.append(stackOperator.pop());
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
                        suffixFormula.append(stackOperator.pop());
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
                    suffixFormula.append(stackOperator.pop());
                    if (stackOperator.empty())
                        break;
                    c1 = stackOperator.peek();
                }
                stackOperator.pop(); // 弹出“(”
            }
        }
        while (!stackOperator.empty())
            suffixFormula.append(stackOperator.pop());
        formula = suffixFormula.toString(); // 将后缀表达式赋予全局计算式,后缀表达式中会保留下划线
    }

    /**
     * 最后的计算方法,得到计算结果
     */
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

    /**
     * 判断传入的char是否为数字
     */
    private boolean isNumber(char c) {
        return (c >= 48 && c <= 57);
    }

}
