

## 反射

### Proxy

```
+----------------------------------------------------------------------------------------------+
|                       Proxy                           InvocationHandler                      |
|                          defineClass0()                        invoke()                      |
|                           h:InvocationHandler                                                |
|                                                                                              |
+----------------------------------------------------------------------------------------------+
|   ProxyGenerator                                Proxy.c                                      |
|        generateProxyClass():byte[]                Java_java_lang_reflect_Proxy_defineClass0()|
|        generateClassFile():byte[]               jni.cpp                                      |
|                                                    jni_DefineClass()                         |
|        proxyMethods:Map                                                                      |
|        addProxyMethod()                         classFileParser.cpp                          |
|        generateConstructor():MethodInfo            parseClassFile()                          |
|        generateStaticInitializer() :MethodInfo                                               |
|        cp:ConstantPool                                                                       |
|        saveGeneratedFiles:boolean                                                            |
|                                                                                              |
+----------------------------------------------------------------------------------------------+
|  DataOutputStream                                      .ConstantPool                         |
|  ByteArrayOutputStream                                                                       |
+----------------------------------------------------------------------------------------------+
|                         //generatorclass:Proxy::Interfaces                                   |
|                          com.sun.proxy.$Proxy4.class:Proxy::CustomInterface                  |
|                                hashcode,tostring,equals:Method                               |
+----------------------------------------------------------------------------------------------+


```


