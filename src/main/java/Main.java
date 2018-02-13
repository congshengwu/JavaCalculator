/**
 * Created by 丛 on 2018/2/12 0012.
 */

public class Main {

    public static void main(String[] args) {
        String result = Calculator.input("50*((6.4+3.6)/2)-50")
                .getResult().stripTrailingZeros().toPlainString();

        System.out.println(result);
    }

}
