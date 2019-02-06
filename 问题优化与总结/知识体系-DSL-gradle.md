#Gradle

Gradle provides a Domain Specific Language (DSL), for describing builds. 

Gradle models its builds as Directed Acyclic Graphs (DAGs) of tasks (units of work). 


```
+-----------------------------------------------------------------------------------------------+
| Build                                                                                         |
|         Build Lifecycle                                                                       |
|                     Initialization    Configuration      Execution                            |
|                                                                                               |
|                                                                 +-----------------------------+
|                                                                 |  configuration files        |
|                                                                 |       build.gradle          |
|                                                                 |       gradle.properties     |
|                                                                 |       settings.gradle       |
+-----------------------------------------------------------------+-----------------------------+
|                                                                                               |
|  Object  +------------------------------------------------------------------+-----------------+
|          | Project                                                          |                 |
|          |     +-------------------------------------+----------------------+  Script         |
|          |     |  properties                         | method               |                 |
|          |     |      project                        |   apply plugins      |                 |
|          |     |      name                           |        script plugins|                 |
|          |     |      path                           |        binary plugins|                 |
|          |     |      description                    |                      |                 |
|          |     |      projectDir                     |                      |                 |
|          |     |      buildDir                       |   getExtensions      |                 |
|          |     |      group                          |                      |                 |
|          |     |      version                        |                      |                 |
|          |     |      ant                            |                      |                 |
|          |     +-------------------------------------+----------------------+                 |
|          |                                                                  |                 |
+----------+------------------------------------------------------------------+-----------------+
|                        Task(similar to ANT’s target)                                          |
|                                                                                               |
|                                             Actions                                           |
|                                             Inputs                                            |
|                                             Outputs                                           |
+-----------------------------------------------------------------------------------------------+
|                          Groovy comes with various AST transformations                        |
+-----------------------------------------------------------------------------------------------+
|               encoded UTF-8                                                                   |
+-----------------------------------------------------------------------------------------------+


```

