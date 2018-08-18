# JavaCalculator
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
