package com.csw.calculator;

/**
 * Created by 丛 on 2018/2/12 0012.
 */

public class Main {

    public static void main(String[] args) {
        String result1 = Calculator.input("8×4-(10+12÷3)").getResult();
        System.out.println(result1);

        String result2 = Calculator.input("8/3").getResult(3); // 保留三位小数
        System.out.println(result2);
    }

}
