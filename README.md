# JavaCalculator
[![](https://jitpack.io/v/congshengwu/JavaCalculator.svg)](https://jitpack.io/#congshengwu/JavaCalculator)  

gradle：
step 1：
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
step 2：
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  String result = Calculator.input("50*((6.4+3.6)/2)-50")
                .getResult().stripTrailingZeros().toPlainString();  
                System.out.println(result);
