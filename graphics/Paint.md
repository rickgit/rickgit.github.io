#Android Paint
##简介
> 

@(源码分析)[Paint|Android]

 

- **创建及初始化**
先用本地方法构造mNativePaint

```java
    public Paint() {
        this(0);
    }
    public Paint(int flags) {
        mNativePaint = native_init();
        setFlags(flags | DEFAULT_PAINT_FLAGS);
        // TODO: Turning off hinting has undesirable side effects, we need to
        //       revisit hinting once we add support for subpixel positioning
        // setHinting(DisplayMetrics.DENSITY_DEVICE >= DisplayMetrics.DENSITY_TV
        //        ? HINTING_OFF : HINTING_ON);
        mCompatScaling = mInvCompatScaling = 1;
        setTextLocale(Locale.getDefault());
    }
```
本地方法位于，创建了本地Paint对象

```c
	//frameworks/base/core/jni/android/graphics/Paint.cpp
    static jlong init(JNIEnv* env, jobject clazz) {
        Paint* obj = new Paint();
        defaultSettingsForAndroid(obj);
        return reinterpret_cast<jlong>(obj); //从指针类型到一个足够大的整数类型
    }

    static void defaultSettingsForAndroid(Paint* paint) {
	    // GlyphID encoding is required because we are using Harfbuzz shaping
	    //  Harfbuzz 是一个开源的text opentype layout 引擎
	    paint->setTextEncoding(Paint::kGlyphID_TextEncoding);
	}
```

Paint* obj = new Paint();对象在frameworks/base/core/jni/android/graphics/PaintImpl.cpp申明，继承external/skia/src/core/SkPaint.cpp类。<br/>
接着看构造方法，最后执行setTextLocale,也是调用frameworks/base/core/jni/android/graphics/Paint.cpp。toLanguageTag生成 bcp47 identifier。

```c
    static void setTextLocale(JNIEnv* env, jobject clazz, jlong objHandle, jstring locale) {
        Paint* obj = reinterpret_cast<Paint*>(objHandle);
        ScopedUtfChars localeChars(env, locale);
        char langTag[ULOC_FULLNAME_CAPACITY];
        toLanguageTag(langTag, ULOC_FULLNAME_CAPACITY, localeChars.c_str());

        obj->setTextLocale(langTag);
    }
```

##setColor
```c
    static void setColor(JNIEnv* env, jobject paint, jint color) {
        NPE_CHECK_RETURN_VOID(env, paint);
        GraphicsJNI::getNativePaint(env, paint)->setColor(color);
    }
```
getNativePaint的实现位于文件frameworks/base/core/jni/android/graphics/Graphics.cpp
```c
	android::Paint* GraphicsJNI::getNativePaint(JNIEnv* env, jobject paint) {
	    SkASSERT(env);
	    SkASSERT(paint);
	    SkASSERT(env->IsInstanceOf(paint, gPaint_class));
	    jlong paintHandle = env->GetLongField(paint, gPaint_nativeInstanceID);
	    android::Paint* p = reinterpret_cast<android::Paint*>(paintHandle);
	    SkASSERT(p);
	    return p;
	}
```
##setStyle
>>The Style specifies if the primitive being drawn is filled, stroked, or both (in the same color). The default is FILL.

同样操作GraphicsJNI::getNativePaint(env, paint)->setColor(color);<br/>
stype有三种类型，位于文件android.graphics.Paint.Style

```java
    public enum Style {
        /**
         * Geometry and text drawn with this style will be filled, ignoring all
         * stroke-related settings in the paint.
         */
        FILL            (0),
        /**
         * Geometry and text drawn with this style will be stroked, respecting
         * the stroke-related fields on the paint.
         */
        STROKE          (1),
        /**
         * Geometry and text drawn with this style will be both filled and
         * stroked at the same time, respecting the stroke-related fields on
         * the paint. This mode can give unexpected results if the geometry
         * is oriented counter-clockwise. This restriction does not apply to
         * either FILL or STROKE.
         */
        FILL_AND_STROKE (2);
        
        Style(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }
```
##setStrokeCap

>> The Cap specifies the treatment for the beginning and ending of stroked lines and paths. The default is BUTT.
StrokeCap类型有三种

```java
    public enum Cap {
        /**
         * The stroke ends with the path, and does not project beyond it.
         */
        BUTT    (0),
        /**
         * The stroke projects out as a semicircle, with the center at the
         * end of the path.
         */
        ROUND   (1),
        /**
         * The stroke projects out as a square, with the center at the end
         * of the path.
         */
        SQUARE  (2);
        
        private Cap(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }
```
##RectF
```java
public class RectF implements Parcelable {
    public float left;
    public float top;
    public float right;
    public float bottom;
    ...
    ...
    ...
}
```
intersect碰撞检测方法，判断两个矩形是否有交集

##Color 
除了定影r,g,b，还定义了 hsb的转换。
- HSB与HSV相同
- HSV(Hue, Saturation, Value)是根据颜色的直观特性由A. R. Smith在1978年创建的一种颜色空间, 也称六角锥体模型(Hexcone Model)。

##BitmapFactory
- 内部类 Option

[Option的相关属性](BitmapFactory#Option.png)

最终生成的Bitmap都是用本地方法
```c
    private static native Bitmap nativeDecodeStream(InputStream is, byte[] storage,
            Rect padding, Options opts);
    private static native Bitmap nativeDecodeFileDescriptor(FileDescriptor fd,
            Rect padding, Options opts);
    private static native Bitmap nativeDecodeAsset(long nativeAsset, Rect padding, Options opts);
    private static native Bitmap nativeDecodeByteArray(byte[] data, int offset,
            int length, Options opts);
    private static native boolean nativeIsSeekable(FileDescriptor fd);
```
本地方法在文件 frameworks/base/core/jni/android/graphics/BitmapFactory.cpp中。
```c
static jobject nativeDecodeStream(JNIEnv* env, jobject clazz, jobject is, jbyteArray storage,
        jobject padding, jobject options) {

    jobject bitmap = NULL;
    SkAutoTUnref<SkStream> stream(CreateJavaInputStreamAdaptor(env, is, storage));

    if (stream.get()) {
        SkAutoTUnref<SkStreamRewindable> bufferedStream(
                SkFrontBufferedStream::Create(stream, BYTES_TO_BUFFER));
        SkASSERT(bufferedStream.get() != NULL);
        bitmap = doDecode(env, bufferedStream, padding, options);
    }
    return bitmap;
}
```
可以看出bitmap由skia解码，先转化为SkStream对象，在获取SkStreamRewindable，最终转化为java bitmap

##Bitmap
Bitmap没有共有(public)构造方法，只能通过BitmapFactory获取。<br/>
包含三个内部类
- android.graphics.Bitmap.Config 
图片质量配置，本地文件在SkBitmap.h
- android.graphics.Bitmap.CompressFormat bitmap的压缩格式，其中WEBP需要api14
- android.graphics.Bitmap.BitmapFinalizer 回收内存Bitmap

