# JavaCalculator
[![](https://jitpack.io/v/congshengwu/JavaCalculator.svg)](https://jitpack.io/#congshengwu/JavaCalculator)
```Java
String result1 = Calculator.input("8×4-(10+12÷3)").getResult();
System.out.println(result1); // 18

String result2 = Calculator.input("8/3").getResult(3);
System.out.println(result2); // 2.667
```
### Gradle
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
```gradle
dependencies {
    implementation 'com.github.congshengwu:JavaCalculator:v1.0'
}
```
### Maven
  ```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    </repositories>
```
  ```xml
<dependency>
    <groupId>com.github.congshengwu</groupId>
        <artifactId>JavaCalculator</artifactId>
        <version>v1.0</version>
</dependency>
