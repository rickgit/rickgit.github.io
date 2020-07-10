## SharedPreference
```
                +----------------------------------------------------------------------------------------+
                |  ContextImpl                                                                           |
                |    mSharedPrefsPaths:ArrayMap<String, File>                                            |
                |    getSharedPreferencesPath(String name): File                                         |
                |                                                                                        |
                |    sSharedPrefsCache                                                                   |
                |         :ArrayMap<String, ArrayMap<File, SharedPreferencesImpl> >                      |
                |    getSharedPreferences():SharedPreferencesImpl                                        |
                |                                                                                        |
                |    getSharedPreferencesCacheLocked()                                                   |
                |         :ArrayMap<File, SharedPreferencesImpl>                                         |
                |                                                                                        |
                +----------------------------------------------------------------------------------------+
                |   SharedPreferencesImpl             mMap:Map<String, Object>        enqueueDiskWrite() |
                |           makeBackupFile():File     edit():EditorImpl               writeToFile()      |
                |           loadFromDisk()                                                               |
                |                             +----------------------------------------------------------+
                |                             | EditorImpl:Editor                                        |
                |                             |    mModified:Map<String, Object>   mEditorLock:Object    |
                |                             |    apply()                         mModified             |
                |                             |    commit()                         :Map<String, Object> | 
                |                             |                                                          |
                |                             | commitToMemory():MemoryCommitResult                      |
                |                             | mListeners                                               |
                |                             |   :WeakHashMap<OnSharedPreferenceChangeListener, Object> |
                +----------------------------------------------------------------------------------------+
                |XmlUtils                     |                                                          |
                |  readMapXml()               |     MemoryCommitResult                                   |
                +-----------------------------+          writtenToDiskLatch //commit() wait return       |
                |Xml                          |                                                          |
                |  newPullParser():KXmlParser |                                                          |
                |                             |                                                          |
                +-----------------------------+                                                          |
                |                             |                                                          |
                | KXmlParser: XmlPullParser   |                                                          |
                ------------------------------+----------------------------------------------------------+


```

##  MMKV for Android


## 
