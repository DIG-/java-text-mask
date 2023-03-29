Simple Text Mask for Java and Android
==================================
[![AppVeyor](https://img.shields.io/appveyor/build/DIG-/java-text-mask/main?logo=appveyor&logoColor=dddddd)](https://ci.appveyor.com/project/DIG-/java-text-mask/branch/main)
[![AppVeyor tests](https://img.shields.io/appveyor/tests/DIG-/java-text-mask/main?logo=appveyor&logoColor=dddddd)](https://ci.appveyor.com/project/DIG-/java-text-mask/branch/main)
[![Maven Central](https://img.shields.io/maven-central/v/br.dev.dig.text.mask/text-mask?label=maven)](https://central.sonatype.com/search?q=br.dev.dig.text.mask)
[![License](https://img.shields.io/static/v1?label=license&message=CC%20BY-ND%204.0&color=blue)](https://creativecommons.org/licenses/by-nd/4.0/)

![Windows - Supported](https://img.shields.io/badge/windows-supported-success?logo=windows&logoColor=dddddd)
![Linux - Supported](https://img.shields.io/badge/linux-supported-success?logo=linux&logoColor=dddddd)
![MacOS - Partial](https://img.shields.io/badge/macos-partial-orange?logo=apple&logoColor=dddddd)

Apply/remove mask from text with some utilities out-of-box for Android EditText

How to use
==========
1. Include maven central as repository
2. Import project dependency
```groovy
dependencies {
    ⋮
    implementation "br.dev.dig.text.mask:text-mask:${lastest_version}"
    // FOR ANDROID:
    implementation "br.dev.dig.text.mask:text-mask-android:${lastest_version}"
    ⋮
}
```
3. Create a `TextMask` instance
```java
final TextMask mask = new TextMask("#### #### #### ####");
final CharSequence formatted = mask.format("12345678");
final CharSequence raw = mask.unformat("1234 5678 1234 5678");
```

For Android
===========
4. Create a `TextMaskTextWatcher` with an `EditText` and a `TextMask`, and apply (insert) into `EditText`.
```java
final TextMaskTextWatcher watcher = new TextMaskTextWatcher(editText, new TextMask("#### #### #### ####")).insert();
```

License
=======
[CC BY-ND 4.0](https://creativecommons.org/licenses/by-nd/4.0/)
- You can use and re-dist freely.
- You can also modify, but only for yourself.
- You can use it as a part of your project, but without modifications in this project.