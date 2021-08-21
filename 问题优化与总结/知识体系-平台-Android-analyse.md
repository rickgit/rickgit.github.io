# 可分析

## 调试工具

adb shell input keyevent   
```java
public class KeyEvent extends InputEvent implements Parcelable {
    public static final int KEYCODE_HOME            = 3;
    /** Key code constant: Back key. */
    public static final int KEYCODE_BACK            = 4;
}
```

## 加密

## 反编译
[dex 版本](https://source.android.google.cn/devices/tech/dalvik/dex-format)
```java
注意：Android 9.0 版本中新增了对 039 版格式的支持，其中引入了两个新字节码 const-method-handle 和 const-method-type。（字节码集合的总结表中介绍了这些字节码。）在 Android 10 中，版本 039 扩展了 DEX 文件格式，以包含仅适用于启动类路径上的 DEX 文件的隐藏 API 信息。

注意：Android 8.0 版本中新增了对 038 版格式的支持。038 版本中添加了新字节码（invoke-polymorphic 和 invoke-custom）和用于方法句柄的数据。

注意：Android 7.0 版本中新增了对 037 版格式的支持。在 037 版本之前，大多数 Android 版本都使用过 035 版格式。035 版与 037 版之间的唯一区别是，是否添加默认方法以及是否调整 invoke。

注意：至少有两种早期版本的格式已在广泛提供的公开软件版本中使用。例如，009 版本已用于 M3 版 Android 平台（2007 年 11 月至 12 月），013 版本已用于 M5 版 Android 平台（2008 年 2 月至 3 月）。在有些方面，这些早期版本的格式与本文档中所述的版本存在很大差异。
```

[dex2jar 废弃 支持 dex 035,036; enjarify 支持35 ](https://github.com/pxb1988/dex2jar)
[jad（不维护）](http://www.kpdus.com/jad.html)
[⭐jadx 支持dex 039](https://github.com/skylot/jadx.git)
[jd-gui](https://github.com/java-decompiler/jd-gui)
[Apktool](https://github.com/iBotPeaches/Apktool)

jeb 调试反编译app