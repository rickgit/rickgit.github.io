# Android DisplayMetrics

> A structure describing general information about a display, such as its size, density, and font scaling.  <p>To access the DisplayMetrics members, initialize an object like this:</p> <pre> DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);</pre>

@(源码分析)[DisplayMetrics|Android]

设计知识 *二分法*

- **创建及初始化**
- **getDeviceDensity方法**

---------------------

[TOC]

## 创建及初始化
相关属性
```
    public static final int DENSITY_LOW = 120;
    public static final int DENSITY_MEDIUM = 160;
    public static final int DENSITY_HIGH = 240;
    public static final int DENSITY_XHIGH = 320;
    public static final int DENSITY_XXHIGH = 480;
    public static final int DENSITY_XXXHIGH = 640;

    public static final int DENSITY_DEFAULT = DENSITY_MEDIUM;
    public static final float DENSITY_DEFAULT_SCALE = 1.0f / DENSITY_DEFAULT;

```
DisplayMetrics只有一个空的构造方法,默认是原型方式，按照原型方式赋值
```
    public void setTo(DisplayMetrics o) {
        widthPixels = o.widthPixels;
        heightPixels = o.heightPixels;
        density = o.density;
        densityDpi = o.densityDpi;
        scaledDensity = o.scaledDensity;
        xdpi = o.xdpi;
        ydpi = o.ydpi;
        noncompatWidthPixels = o.noncompatWidthPixels;
        noncompatHeightPixels = o.noncompatHeightPixels;
        noncompatDensity = o.noncompatDensity;
        noncompatDensityDpi = o.noncompatDensityDpi;
        noncompatScaledDensity = o.noncompatScaledDensity;
        noncompatXdpi = o.noncompatXdpi;
        noncompatYdpi = o.noncompatYdpi;
    }
    
    public void setToDefaults() {
        widthPixels = 0;
        heightPixels = 0;
        density =  DENSITY_DEVICE / (float) DENSITY_DEFAULT;
        densityDpi =  DENSITY_DEVICE;
        scaledDensity = density;
        xdpi = DENSITY_DEVICE;
        ydpi = DENSITY_DEVICE;
        noncompatWidthPixels = widthPixels;
        noncompatHeightPixels = heightPixels;
        noncompatDensity = density;
        noncompatDensityDpi = densityDpi;
        noncompatScaledDensity = scaledDensity;
        noncompatXdpi = xdpi;
        noncompatYdpi = ydpi;
    }

```
下面初始化对象并赋值
```
DisplayMetrics metrics = new DisplayMetrics();
getWindowManager().getDefaultDisplay().getMetrics(metrics);
```
android.view.Display#getMetrics
```
    public void getMetrics(DisplayMetrics outMetrics) {
        synchronized (this) {
            updateDisplayInfoLocked();
            mDisplayInfo.getAppMetrics(outMetrics, mDisplayAdjustments);
        }
    }
```

下面我们看下Display的由来
1. android.view.Display#Display获取是由WindowManagerImpl获取到的，首次是第一个构造方法，两个参数的构造方法是在activity#onAttach方法执行时候调用WindowManagerImpl#createLocalWindowManager方法调用的
```
    public WindowManagerImpl(Display display) {
        this(display, null);
    }

    private WindowManagerImpl(Display display, Window parentWindow) {
        mDisplay = display;
        mParentWindow = parentWindow;
    }

    public WindowManagerImpl createLocalWindowManager(Window parentWindow) {
        return new WindowManagerImpl(mDisplay, parentWindow);
    }

    public WindowManagerImpl createPresentationWindowManager(Display display) {
        return new WindowManagerImpl(display, mParentWindow);
    }
```
2. 第一个构造方法是有static的静态块执行的。可以看出默认是使用ContextImpl的Display，再次使用mDefaultDisplay,getOutContext()获取是由android.app.LoadedApk#makeApplication首次创建的ContextImpl对象，这里不深究了。看下DisplayManager如何创建Display的
```
        registerService(WINDOW_SERVICE, new ServiceFetcher() {
                Display mDefaultDisplay;
                public Object getService(ContextImpl ctx) {
                    Display display = ctx.mDisplay;
                    if (display == null) {
                        if (mDefaultDisplay == null) {
                            DisplayManager dm = (DisplayManager)ctx.getOuterContext().
                                    getSystemService(Context.DISPLAY_SERVICE);
                            mDefaultDisplay = dm.getDisplay(Display.DEFAULT_DISPLAY);
                        }
                        display = mDefaultDisplay;
                    }
                    return new WindowManagerImpl(display);
                }});

```
3. android.hardware.display.DisplayManager#getDisplay只执行了getOrCreateDisplayLocked方法，方法里面mGlobal.getCompatibleDisplay正式Display创建的地方。
```
    private Display getOrCreateDisplayLocked(int displayId, boolean assumeValid) {
        Display display = mDisplays.get(displayId);
        if (display == null) {
            display = mGlobal.getCompatibleDisplay(displayId,
                    mContext.getDisplayAdjustments(displayId));
            if (display != null) {
                mDisplays.put(displayId, display);
            }
        } else if (!assumeValid && !display.isValid()) {
            display = null;
        }
        return display;
    }

```
4. android.hardware.display.DisplayManagerGlobal#getCompatibleDisplay
```
    public Display getCompatibleDisplay(int displayId, DisplayAdjustments daj) {
        DisplayInfo displayInfo = getDisplayInfo(displayId);
        if (displayInfo == null) {
            return null;
        }
        return new Display(this, displayId, displayInfo, daj);
    }
```
DisplayMetrics是由displayInfo获取到的，上面的代码可以看出是从getDisplayInfo获取。 mDm是IDisplayManager.Stub.asInterface(b)，android.hardware.display.IDisplayManager对象
```
               info = mDm.getDisplayInfo(displayId);
```
```
    public static DisplayManagerGlobal getInstance() {
        synchronized (DisplayManagerGlobal.class) {
            if (sInstance == null) {
                IBinder b = ServiceManager.getService(Context.DISPLAY_SERVICE);
                if (b != null) {
                    sInstance = new DisplayManagerGlobal(IDisplayManager.Stub.asInterface(b));
                }
            }
            return sInstance;
        }
    }
```
至此，已经了解到了DisplayMetrics的由来。
## getDeviceDensity方法
android.util.DisplayMetrics#getDeviceDensity
```
    private static int getDeviceDensity() {
        // qemu.sf.lcd_density can be used to override ro.sf.lcd_density
        // when running in the emulator, allowing for dynamic configurations.
        // The reason for this is that ro.sf.lcd_density is write-once and is
        // set by the init process when it parses build.prop before anything else.
        return SystemProperties.getInt("qemu.sf.lcd_density",
                SystemProperties.getInt("ro.sf.lcd_density", DENSITY_DEFAULT));
    }
```
注释描述比较清楚
```
    public static int getInt(String key, int def) {
        if (key.length() > PROP_NAME_MAX) {
            throw new IllegalArgumentException("key.length > " + PROP_NAME_MAX);
        }
        return native_get_int(key, def);
    }
```
具体是用native方法实现的（frameworks/base/core/jni/android_os_SystemProperties.cpp）
```
static jint SystemProperties_get_int(JNIEnv *env, jobject clazz,
                                      jstring keyJ, jint defJ)
{
    int len;
    const char* key;
    char buf[PROPERTY_VALUE_MAX];
    char* end;
    jint result = defJ;

    if (keyJ == NULL) {
        jniThrowNullPointerException(env, "key must not be null.");
        goto error;
    }

    key = env->GetStringUTFChars(keyJ, NULL);

    len = property_get(key, buf, "");
    if (len > 0) {
        result = strtol(buf, &end, 0);
        if (end == buf) {
            result = defJ;
        }
    }

    env->ReleaseStringUTFChars(keyJ, key);

error:
    return result;
}
```
property_get方法位于system/core/libcutils/properties.c
```
int property_get(const char *key, char *value, const char *default_value)
{
    int len;

    len = __system_property_get(key, value);
    if(len > 0) {
        return len;
    }
    if(default_value) {
        len = strlen(default_value);
        if (len >= PROPERTY_VALUE_MAX) {
            len = PROPERTY_VALUE_MAX - 1;
        }
        memcpy(value, default_value, len);
        value[len] = '\0';
    }
    return len;
}
```
__system_property_get代码位于bionic/libc/bionic/system_properties.cpp,下面是具体的查找方法
```
/*
 * Properties are stored in a hybrid trie/binary tree structure.
 * Each property's name is delimited at '.' characters, and the tokens are put
 * into a trie structure.  Siblings at each level of the trie are stored in a
 * binary tree.  For instance, "ro.secure"="1" could be stored as follows:
 *
 * +-----+   children    +----+   children    +--------+
 * |     |-------------->| ro |-------------->| secure |
 * +-----+               +----+               +--------+
 *                       /    \                /   |
 *                 left /      \ right   left /    |  prop   +===========+
 *                     v        v            v     +-------->| ro.secure |
 *                  +-----+   +-----+     +-----+            +-----------+
 *                  | net |   | sys |     | com |            |     1     |
 *                  +-----+   +-----+     +-----+            +===========+
 */
```
详细请阅读[Android 系统属性SystemProperty分析](http://www.cnblogs.com/bastard/archive/2012/10/11/2720314.html)