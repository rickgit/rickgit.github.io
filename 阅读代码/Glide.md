```

----------------+---------------------------------------------------------------------+--------------------------------------------+---------------------------------+--------------+-----------------------------------------+
|               |            |assets  raw  drawable ContentProvider| Picking a        |     Glide.with(fragment).asDrawable()      |                                 |              |                                         |
|               |ModelLoaders|SD   http/https                      | resource type    |                                            |                                 |              |                                         |
|               +-----------------------------------------------------------------------------------------+------------------------+                                 |              |                                         |
|    Component  |                                                  |                  |                   |   Placeholder          |                                 |              |                                         |
|    Options    |   ResourceEncoders, Encoders                     |                  |  placeholders     |   Error                |                                 |              |                                         |
|    load()     |   ResourceDecoders,                              |                  |                   |   Fallback             |                                 |              |                                         |
+------------------------------------------------------------------+                  +--------------------------------------------+                                 |              |                                         |
|  Application  |  Memory cache   LruResourceCache                 |  Request         | Transformations   |  circleCrop CenterCrop |                                 |              |                                         |
|  Options      |                                                  |  options         +--------------------------------------------+                                 |              |                                         |
|               |  Bitmap pool    LruBitmapPool                    |                  |  Caching          |                        |                                 |              |                                         |
|               |  Disk Cache     DiskLruCacheWrapper              |  apply()         |  Strategies       |                        |                                 |              |                                         |
|               +--------------------------------------------------+                  +--------------------------------------------+                                 |              |                                         |
|               | Default         format(DecodeFormat.RGB_565)     |                  |  Component        |                        |  View fade in                   |              |                                         |
|               | Request Options disallowHardwareBitmaps()        |                  |  specific         |                        |  Cross fade from the placeholder|  into()  size|  GlideExtension                         |
|               | UncaughtThrowableStrategy                        +------------------+-------------------+------------------------+  No transition                  |              |               GlideOption               |
|               | Log le^el                                        |  Thumbnail                                                    |                                 |              |               GlideType (GIFs, SVG etc) |
|               +------------------------------------------------------------------------------------------------------------------+                                 |              |                                         |
|               |  GlideModule                                     |  error                                                        |                                 |              |                                         |
+---------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
|                                                                  |                                                               |                                 |              |                                         |
|                                                                  |                 Request                                       |    Transition                   |   Targets    |     Generated API                       |
|             Configuration                                        |                 Builder                                       |                                 |              |                                         |
+------------------------------------------------------------------+---------------------------------------------------------------+---------------------------------+--------------+-----------------------------------------+



  001_initial_code          1903a3ba2b980fd5c0556bfe869333a34411f5f5 initial commit of source from project
  002_presenter_imageloader f9a436a1bfb5e4b6901506ea61dc490a9b2fe5ae Add presenter system for wrapping imageviews
  003_pathLoader            1afd6153d474f6f54a9b42d0df263e48ffaf4154 Refactor ImageLoader -> PathLoader + ImageLoader
  004_start_stop_diskcache  b362a3df8471b8d8716b5747690b492e8fb2a984 Add start/stop to disk cache
* 005_flickr_sample         51c51e7fa4d247864f3debfff8aaff4078aaac7b Initial commit of flickr sample
                            +-------------------------------------------------------------------+
                            |  diskCache                                                        |
                            |                                                                   |
                            |  memoryCache  mainHandler                                         |
                            |                                                                   |
                            |  bitmapCache  executor    resizer                     pathLoader  |
                            |                                                       imageLoader |
                            |                                                                   |
                            +----------------------------------------+--------------------------+
                            |      ImageManager                      |            ImagePresenter|
                            |                                        |                          |
                            +----------------------------------------+--------------------------+


  200_v2.0_a                64186e3971a8f9ec14a8da2bec752e8182a2057a Bump targetSdkVersion
                            +----------------------------------------+--------------------------+
                            |                                        |      pathLoader          |
                            |  diskCache                             |      imageLoader         |
                            |                                        |                          |
                            |  memoryCache  mainHandler              +--------------------------+
                            |                                        |      ImagePresenter      |
                            |  bitmapCache  executor    resizer      |                          |
                            |                                        +--------------------------+
                            |                                        |       ModelLoader        |
                            +----------------------------------------+                          |
                            |      ImageManager                      |   volley.RequestQueue    |
                            |                                        |                          |
                            +----------------------------------------+--------------------------+
                            |                         Glide                                     |
                            +-------------------------------------------------------------------+


 490_v4.9.0                3035749168c8a4187cf3a51d19a6aee3bc5958d1 Bump version to 4.9.0
                          +------------------------------------+------------------------------+
                          |                   resourceRecycler |   DiskCache                  |
                          |                   diskCacheProvider|   MemoryCache                |
                          |                   memoryCache      |   BitmapPool                 |
                          |                          engine    |   arrayPool                  |
                          +-------------------------------------------------------------------+
                          |   ViewTarget       Request         |                              |
                          |      :Target                       |                              |
                          +------------------------------------+                              |
                          |  RequestBuilder                    |                              |
                          |                                    |                              |
                          |GlideRequests                       |                              |
                          | :RequestManager                    |                              |
                          +------------------------------------+                              |
                          |          Glide                                                    |
                          +-------------------------------------------------------------------+

```