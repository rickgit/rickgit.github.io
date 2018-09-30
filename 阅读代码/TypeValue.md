# Android TypeValue分析

> 在Android中，我们可以在values文件夹中定义各种资源，其中有一种就是dimension。
dimension是一个包含单位（dp、dip、sp、pt、px、mm、in）的尺寸，可以用于定义视图的宽度、字号等。如下图所示。[Android Dimension转换算法原理分析](http://johnsonxu.iteye.com/blog/1929027)

@(源码分析)[TypeValue|Android]

 

- **创建及初始化**
- **complexToDimension方法** 

---------------------

[TOC]

## 创建及初始化


## complexToDimension方法
-- android.content.res.Resources#getDimension
-- android.view.animation.ScaleAnimation#resolveScale



由下面的数据可以看出data数组是以6个为一组。
```

    private boolean getValueAt(int index, TypedValue outValue) {
        final int[] data = mData;
        final int type = data[index+AssetManager.STYLE_TYPE];
        if (type == TypedValue.TYPE_NULL) {
            return false;
        }
        outValue.type = type;
        outValue.data = data[index+AssetManager.STYLE_DATA];
        outValue.assetCookie = data[index+AssetManager.STYLE_ASSET_COOKIE];
        outValue.resourceId = data[index+AssetManager.STYLE_RESOURCE_ID];
        outValue.changingConfigurations = data[index+AssetManager.STYLE_CHANGING_CONFIGURATIONS];
        outValue.density = data[index+AssetManager.STYLE_DENSITY];
        outValue.string = (type == TypedValue.TYPE_STRING) ? loadStringValueAt(index) : null;
        return true;
    }
//data数组
//[AssetManager.STYLE_TYPE][AssetManager.STYLE_DATA][AssetManager.STYLE_ASSET_COOKIE][AssetManager.STYLE_RESOURCE_ID][AssetManager.STYLE_CHANGING_CONFIGURATIONS][index+AssetManager.STYLE_DENSITY]
//[AssetManager.STYLE_TYPE][AssetManager.STYLE_DATA][AssetManager.STYLE_ASSET_COOKIE][AssetManager.STYLE_RESOURCE_ID][AssetManager.STYLE_CHANGING_CONFIGURATIONS][index+AssetManager.STYLE_DENSITY]
```