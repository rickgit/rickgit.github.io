## 内存篇
LeakCanary&shark
```
* 001_initial f0cc04dfbf3cca92a669f0d250034d410eb05816 Initial import
                +-----------------------------------------------------------------------------------------------------+------------------------------+
                |                                                LeakCanary                                           |                              |
                |                                                   install(app:Application)                          |                              |
                +------------------------------------------------------------------------------------------------------------------------------------+
                |                                 RefWatcher                                                          |                              |
                |                                     queue:ReferenceQueue                                            |                              |
                |                                     retainedKeys:Set<String>                                        |                              |
                |                                                                                                     |                              |
                |                                     watch()                                                         |                              |
                |                                     removeWeaklyReachableReferences()                               |                              |
                |                                     gone()                                                          |                              |
                +------------------------------------------------------------------------------------------------------------------------------------+
                | AndroidWatchExecutor    GcTrigger   AndroidHeapDumper    ServiceHeapDumpListener: HeapDump.Listener |                              |
                |                            runGc()       dumpHeap()          analyze(h:HeapDump)                    |                              |
                |                                                                                                     |                              |
                | AndroidDebuggerControl              Debug                HeapAnalyzerService                        |                              |
                |                                        dumpHprofData()     heapAnalyzer:HeapAnalyzer                |                              |
                |                              dalvik_system_VMDebug.c       runAnalysis()                            | DisplayLeakActivity          |
                |           Dalvik_dalvik_system_VMDebug_dumpHprofData()     listenerServiceClass                     |                              |
                |                                                              :DisplayLeakService                    | AbstractAnalysisResultService|
                |                                                                                                     |                              |
                +------------------------------------------------------------------------------------------------------------------------------------+
                |                                         HeapDump               HeapAnalyzer                         |                              |
                |                                           heapDumpFile:File        checkForLeak():AnalysisResult    |                              |
                |                                                                    findLeakTrace():AnalysisResult   |                              |
                +------------------------------------------------------------------------------------------------------------------------------------+
                |                                                              proj(:haha-1.1)                        |                              |
                |                                                                                                     |                              |
                +-----------------------------------------------------------------------------------------------------+------------------------------+

                +----------------------------------------------------------------------------------------------------------------+ 
                | [haha]                                    SnapshotFactory                                                      |
                |                                                  openSnapshot():ISnapshot                                      | 
                |                                           Snapshot                                                             |
                |                                               createSnapshot():ISnapshot                                       | 
                |                                           SnapshotImpl                                                         |
                |                                               readFromFile():ISnapshot                                         |
                |                                               parse():ISnapshot                                                | 
                +----------------------------------------------------------------------------------------------------------------+
 
* 200_v2.0    49510378fa14e14e110985da4cab838facbf4864 Prepare 2.0 release
使用[shark]代理[haha]作为dump parse

                +-----------------------------------------------------------------------------------------------------+
                |[leakcanary-object-watcher-android]                                                                  |
                |                          sealed AppWatcherInstaller:ContentProvider()                               |
                +-----------------------------------------------------------------------------------------------------+
                |                                   InternalAppWatcher                                                |
                |                                                                                                     |
                |        ActivityDestroyWatcher                 FragmentDestroyWatcher                                |
                |                                                                                                     |
                +-----------------------------------------------------------------------------------------------------+
                | [leakcanary-object-watcher]                                                                         |
                |                                ObjectWatcher                                                        |
                +-----------------------------------------------------------------------------------------------------+
                |  [leakcanary-android-core]                                                                          |
                |                            HeapDumpTrigger                                                          |
                |                                 onObjectRetained()                                                  |
                |                                 heapDumper: HeapDumper                                              |
                |                                                                                                     |
                |                            AndroidHeapDumper                                                        |
                |                                  dumpHeap()                                                         |
                |                                                                                                     |
                |                             Debug                                                                   |
                |                               dumpHprofData()                                                       |
                +-----------------------------------------------------------------------------------------------------+
                |   [shark]                                                                                           |
                |             HeapAnalyzer                 FindLeakInput                                              |
                |                 analyze()                      findLeaks()                                          |
                |                                                buildLeakTraces()                                    |
                +-----------------------------------------------------------------------------------------------------+
                |        [shark-hprof]                      [shark-graph]                                             |
                |             Hprof                             HprofHeapGraph                                        |
                |               open(hprofFile: File)                                                                 |
                +-----------------------------------------------------------------------------------------------------+

```

### [Studio Profiler](https://github.com/JetBrains/android/tree/master/profilers/src/com/android/tools/profilers/memory)
