## TDD 测试驱动


## Junit
```
+----------------------------------------------------------------+ 
| TestCase   TestSuite     Filter Sorter  TestClass              |
+----------------------------------------------------------------+
|          framework              runner             extendtions |
+----------------------------------------------------------------+ 
|                  junit.textui.TestRunner.run                   |
+----------------------------------------------------------------+ 
|  Decorator Pattern         Visitor Pattern         Adapter     | 
+----------------------------------------------------------------+
|                            Hamcrest                            |
+----------------------------------------------------------------+ 
|                            JUnit4                              |
+----------------------------------------------------------------+ 


mockito
+---------------------------------------------------------------+
|                 cglib objenesis junit                         |
+---------------------------------------------------------------+
|                        mockito                                |
+---------------------------------------------------------------+


cglib 
 Byte Code Generation Library is high level API to generate and transform Java byte code. It is used by AOP, testing, data access frameworks to generate dynamic proxy objects and intercept field access. 

  001_initial      9d15dedc724446e12f6e1d9f97471708fc7ae994 Initial revision
  002_bcel         fee66acdedf44009f9618425da5b7bdbbb9c1e8c *** empty log message ***
                  +-------------------------------------------------------+
                  |                   bcel.jar                            |
                  +-------------------------------------------------------+
                  |                        cglib                          |
                  +-------------------------------------------------------+
  003_readme       f04a610f4d164b054b7078b1fc0c95b3a6423707 Added readme
  004_test         df1538d486a2f57fce60de05055313ebfd7ec860 Added tests
  005_sample       f5a681c5a6a5808313d1adf3d73def2d96f9333f Added sample
  006_serializable aa412f71a4e8f607144811c2e1e3fba0922b3401 Added implementation for serializable enhancment
  007_cinit        6f8ce327f3ea3c609e861a9df7f81d63592703ae started cinit implementation
  008_comment      66c878866633e77188afad07970a139909edf4c4 added some comments
  009_validation   863c8221db914bf50f403da63617dee2bff156a6 added some validation code
* 010_proposals    145148c36e89576ddbe9da433cc25386f96b4454 added directory for proposals



objenesis
it's pretty easy to instantiate objects in Java through standard reflection. 
  001_initial             f52ba8f8e025425f7ca51a372ea5abb37e58677b Initial checkin of Objenesis TCK
  002_reflectionfactory   5a3449d3f79871eba7ff64c33345966d963d759f Added new ObjectInstantiator that uses sun.reflect.ReflectionFactory. This is built separately from 'core' as it is not available to all platforms. If it's not available, the build just carries on without it.
  003_additions           af4369b3adee79a599927e55f685ff6626d11ce3 TCK additions: * TCK candidate classes now read from config file, rather than being hard coded in main(). * ObjectInstantiators are loaded dynamically and fail gracefully, so if any can't be loaded on a particular platform, the rest of the TCK can still be run. * Added some more candidate classes.
  004_classInstantiator   0b42ce9a20aab39709cc412f2c0ed961df511470 Adding ClassInstantiator
  005_jdk1.3_instantiator 9b005fac2efa62dedf4c9f458f2ed22fb29eb823 Added:    * Sun JDK 1.3 Instantiator
* 006_maven               5e1ccf48fcd0a03c475923f679582ad585c86b4e Base maven pom


```
## repo
+--------------------------------------------------+
|                                                  |
|     repo         Manifest     AOSP project       |
|                                                  |
|                     repo                         |
+--------------------------------------------------+


