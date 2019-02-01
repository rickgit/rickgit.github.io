##  DOS Batch Files script

>A batch file is a kind of script file in DOS, OS/2 and Microsoft Windows.
with commands that get executed in sequence, one after the other. 
These files have the special extension BAT or CMD. 
When a batch file is run, the shell program (usually COMMAND.COM or cmd.exe/CMD shell, PowerShell) reads the file and executes its commands, normally line-by-line.

弱类型动态类型

[batch script tutorial](https://www.tutorialspoint.com/batch_script/batch_script_variables.htm)
内置命令或符号(关键字)
```
+----------------------------------------------------------------------+
|          |                                                           |
|  system  | ver  vol chkdsk cmd convert driverquery find              |
|          |  help ipconfig label net ping shutdown subst systeminfo   |
|          |  taskkill tasklist  diskpart                              |
+----------------------------------------------------------------------+
|          |                                                           |
|  shell   |  cls exit pause prompt start title                        |
|          |   rem  :: echo  @echo off                                 |
|          |                                                           |
|          |                                                           |        
|----------+-----------------------------------------------------------+
|  file    |  assoc copy del md move rd  type attrib comp expand       |
|          |  more  sort xcopy fc                                      |
|          |                                                           |
|          |                                                           |
+----------------------------------------------------------------------+
|          |                                                           |
|  find    |  cd dir path find tree                                    |
|          |                                                           |
|          |                                                           |
|          |                                                           |
+----------------------------------------------------------------------+
| Decision |   if else  goto                                           |
| Making   |                                                           |
+----------------------------------------------------------------------+
| datatype | %% []   date   time                                       |
|          |                                                           |
+----------------------------------------------------------------------+
|          |                                                           |
|  var     |   set  SETLOCAL/ENDLOCAL                                  |
+----------+-----------------------------------------------------------+

```

分隔符
```bat
Comma (,)
Semicolon (;)
Equals (=) 
Space ( )
Tab (     ) 
```
注释：rem  ::
 
类型：
String: %%
Arrays：[]
```
+----------------------------------------------------------------------+
|Arr       | []                                                        |
|Operators |                                                           |
|          |                                                           |
+----------------------------------------------------------------------+
|String    |  %%                                                       |
|Operators |                                                           |
|          |                                                           |
+----------+-----------------------------------------------------------+
|          |                                                           |
|Bitwise   |  & | ^                                                    |
|Operators |                                                           |
|          |                                                           |
+----------------------------------------------------------------------+
|          |                                                           |
|Assignment|  += -= *= /= %= md move rd rem type attrib comp expand    |
|Operators |  more  sort xcopy fc                                      |
|          |                                                           |
|          |                                                           |
+----------------------------------------------------------------------+
|          |                                                           |
|Logical   |  And OR NOT                                               |
|Operators |                                                           |
|          |                                                           |
+----------------------------------------------------------------------+
|          |                                                           |
|Relational|  EQU NEQ LSS LEQ GTR GEQ                                  |
|Operators |                                                           | 
+----------------------------------------------------------------------+
|          |                                                           |
|Arithmetic|                                                           |
|Operators |    + - * / %                                              |
|          |                                                           | 
+----------+-----------------------------------------------------------+

```
## powershell


## shell script
弱类型动态类型脚本语言
[教程](https://www.tutorialspoint.com/unix/unix-file-system.htm)
内置命令或符号(关键字)
```
+----------+-----------------------------------------------------------+
|  system  |                                                           |
|          |                                                           |
|          |                                                           |
|          |                                                           |
+----------------------------------------------------------------------+
|  user    |                                                           |
|          |                                                           |
|          |                                                           |
|          |                                                           |
+------------------------+---------------------------------------------+
|  file    | Navigating  |  cat  cd    cp    file   find  head  less ls|
|          |             |  mkdir  more  mv  pwd   rm   rmdir  tail    |
|          |             |                                             |
|          |             |  touch   touch   which                      |
|          +-----------------------------------------------------------+
|          |             |                                             |
+------------------------+---------------------------------------------+
|  Regular |  sed                                                      |
|Expression|                                                           |
+----------------------------------------------------------------------+
|          |                                                           |
|  loop    |  do...while for until select                              |
|          |                                                           |
|          |                                                           |
+----------------------------------------------------------------------+
| Decision |   if...fi                                                 |
| Making   |   if...else...fi                                          |
|          |   if...elif...else...fi                                   |
|          |   case...esac                                             |
+----------+-----------------------------------------------------------+


```

```
+----------------------------------------------------------------------+
|Arr       | []                                                        |
|Operators |                                                           |
|          |                                                           |
+----------+-----------------------------------------------------------+
|          |                                                           |
|korn Shell|  simulate to c Shell operators                            |
|Operators |                                                           |
|          |                                                           |
+-----------------------+----------------------------------------------+
|          | Arithmetic |   <<    >>                                   |
|C Shell   |            |   ( )  ~      &  ^  |  &&  ||  ++            |
|Operators |                                                           |
|          +------------+----------------------------------------------+
|          | Logical    |                                              |
|          |            |                                              |
+-----------------------+----------------------------------------------+
|          |                                                           |
|File Test |  +b +c +d +g +k +p +t +u +r +w +x +s +e                   |
|Operators |                                                           |
|          |                                                           |
+----------------------------------------------------------------------+
|          |                                                           |
|String    |  = != +Z                                                  |
|Operators | +n str                                                    |
+----------------------------------------------------------------------+
|          |                                                           |
|Boolean   |  ! +0 +a                                                  |
|Operators |                                                           |
+----------------------------------------------------------------------+
|          |                                                           |
|Relational|  +eq +ne +gt +lt +ge +le                                  |
|Operators |                                                           |
+----------------------------------------------------------------------+
|          |                                                           |
|Arithmetic|                                                           |
|Operators |    + + * / %  = == !=                                     |
|          |                                                           |
+----------+-----------------------------------------------------------+



```

## bash