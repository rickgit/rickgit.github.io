+--------------------------------------------------------------------------+
|                            AdblockPlus:App                               |
|                                js:JSThread                               |
|                                subscriptions:List<Subscription>          |
|                                                                          |
|                           JSThread:Thread                                |
|                               jsEngine:JSEngine                          |
|                                                                          |
|                           JSEngine                                       |
|                                nativeInitialize()                        |
|                               nativeExecute()                            |
|                               methods:ObjMethod[]                        |
|                                                                          |
|                                                                          |
|                                      [v8] v8::Handle< v8::String>        |
|                                                                          |
|                                           v8::Handle< v8::Script>        |
|                                                  Run()                   |
+--------------------------------------------------------------------------+
|   [js]                                                                   |
|         start.js                                                         |
|    XMLHttpRequest.jsm  FilterNotifier.jsm  FilterClasses.jsm             |
|   SubscriptionClasses.jsm   FilterStorage.jsm   FilterListener.jsm       |
|    Matcher.jsm          Synchronizer.jsm                                 |
+--------------------------------------------------------------------------+
