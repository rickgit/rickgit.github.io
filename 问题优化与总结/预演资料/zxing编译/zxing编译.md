[Zxing编译](https://github.com/zxing/zxing/wiki/Getting-Started-Developing)


## 获取

1. 编译
```shell
set ANDROID_HOME=D:\Program\Android\sdk
mvn install -DskipTests -Drat.skip=true
```
编译出
core-x.y.z.jar android-core-x.y.z.jar

2. 下载
[下载路径](http://repo1.maven.org/maven2/com/google/zxing/)


## Zxing 架构
```
+--------------------------------------------------------------------+ 
|  android                                                           |
|                                                                    | 
|    ViewfinderView           CameraManager                          |
|                                                                    |
|              CaptureActivityHandler                                |
|                                                BeepManager         |
|                   DecodeThread                 AmbientLightManager |
|                                                                    |
|                                                                    |
+--------------------------------------------------------------------+
| amdroid-core                              CameraConfigurationUtils |
|                                                                    |
+--------------------------------------------------------------------+
| zxing-core                                                         |
|               QRCodeReader  DataMatrixReader   AztecReader         |
|               PDF417Reader  MaxiCodeReader                         |
|                                                                    |
|               BinaryBitmap                                         |
|               HybridBinarizer                                      |
|               GlobalHistogramBinarizer                             |
+--------------------------------------------------------------------+

```

### 扫描算法

[十三种基于直方图的图像全局二值化算法原理、实现、代码及效果。](https://www.cnblogs.com/Imageshop/p/3307308.html)


### QRCODE
![QR symbol illustrating ](1920px-QR_Ver3_Codeword_Ordering.svg.png)

```
+--------------------------------+----------------------------------------------------------+
|                                |                   Bit order                              |
|                                +---------------------------+------------------------------+
|                                |      D1-D13               |       D14-D26                |
|                                |                           |                              |
|                                |      E1-E22               |       E23-E44                |
|                                +----------------------------------------------------------+
|                                |     Block 1 Codewords     |   Block 2 Codewords          |
|                                +---------------------------+------------------------------+
|                                |      Error Correction Level H                            |
|                                |     ( Reed–Solomon error correction algorithm)           |
+-------------------------------------------------------------------------------------------+
|  Fixed Patterns   Format Info  |     D:data     E:error correction     X:unused           |
+--------------------------------+----------------------------------------------------------+

```

[定点对焦的代码](https://www.cnblogs.com/sickworm/p/4562081.html)
```
//定点对焦的代码   
     private void pointFocus(int x, int y) {
                if (cameraParameters.getMaxNumMeteringAreas() > 0) {
                        List<Camera.Area> areas = new ArrayList<Camera.Area>();
                        Rect area = new Rect(x - 100, y - 100, x + 100, y + 100);
                        areas.add(new Camera.Area(area, 600));
                        cameraParameters.setMeteringAreas(areas);
                }
                mCamera.cancelAutoFocus();
                cameraParameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
                mCamera.setParameters(cameraParameters);
                mCamera.autoFocus(autoFocusCallBack);
        }

```