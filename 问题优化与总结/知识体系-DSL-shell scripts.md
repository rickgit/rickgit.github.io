##  DOS Batch Files script

>A batch file is a kind of script file in DOS, OS/2 and Microsoft Windows.
with commands that get executed in sequence, one after the other. 
These files have the special extension BAT or CMD. 
When a batch file is run, the shell program (usually COMMAND.COM or cmd.exe/CMD shell, PowerShell) reads the file and executes its commands, normally line-by-line.

弱类型动态类型

[batch script tutorial](https://www.tutorialspoint.com/batch_script/batch_script_variables.htm)
内置命令或符号(关键字)
```
+----------+-----------------------------------------------------------+
|          |                                                           |
|  system  | ver  vol chkdsk cmd convert driverquery find              |
|          |  help ipconfig label net ping shutdown subst systeminfo   |
|          |  taskkill tasklist  diskpart                              |
+----------------------------------------------------------------------+
|  shell   |  cls exit pause prompt start title                        |
|          |   rem  :: echo  @echo off                                 |
+----------------------------------------------------------------------+
|  file    |  assoc copy del md move rd  type attrib comp expand       |
|          |  more  sort xcopy fc                                      |
+----------------------------------------------------------------------+
|  find    |  cd dir path find tree                                    |
+----------------------------------------------------------------------+
| Decision |   if else  goto                                           |
| Making   |                                                           |
+----------------------------------------------------------------------+
| datatype | %% []   date   time                                       |
+----------------------------------------------------------------------+
|  var     |   set  SETLOCAL/ENDLOCAL                                  |
+-------+--+--------+-----------------+-------------+------------------+
|       |           |                 |             |                  |
| $PATH | alias -p  | shell-variable  | shell-func  |   shell-command  |
+-------+-----------+-----------------+-------------+------------------+
|                       Linux Shell                                    |
+----------------------------------------------------------------------+
|                       Bash                                           |
+----------------------------------------------------------------------+


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


```

::设置参数
set var="var"


 ::命令行输入参数
set /p var= 


::内置参数
%0 文件名
%1 参数1


::输出到文件
echo abc >a.txt
echo abc_cd >>a.txt
```
## powershell


## shell script
弱类型动态类型脚本语言
[教程](https://www.tutorialspoint.com/unix/unix-file-system.htm)
内置命令或符号(关键字)
```
+----------+-----------------------------------------------------------+
|  system  |                                                           |
+----------------------------------------------------------------------+
|  user    |                                                           |
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
+----------+-----------------------------------------------------------+
|korn Shell|  simulate to c Shell operators                            |
|Operators |                                                           |
+-----------------------+----------------------------------------------+
|          | Arithmetic |   <<    >>                                   |
|C Shell   |            |   ( )  ~      &  ^  |  &&  ||  ++            |
|Operators +------------+----------------------------------------------+
|          | Logical    |                                              |
+-----------------------+----------------------------------------------+
|File Test |  +b +c +d +g +k +p +t +u +r +w +x +s +e                   |
|Operators |                                                           |
+----------------------------------------------------------------------+
|String    |  = != +Z                                                  |
|Operators | +n str                                                    |
+----------------------------------------------------------------------+
|Boolean   |  ! +0 +a                                                  |
|Operators |                                                           |
+----------------------------------------------------------------------+
|Relational|  +eq +ne +gt +lt +ge +le                                  |
|Operators |                                                           |
+----------------------------------------------------------------------+
|Arithmetic|                                                           |
|Operators |    + + * / %  = == !=                                     |
+----------+-----------------------------------------------------------+



```

shell-if

```
if [ -f file ] 如果文件存在
if [ -d … ] 如果目录存在
if [ -s file ] 如果文件存在且非空
if [ -r file ] 如果文件存在且可读
if [ -w file ] 如果文件存在且可写
if [ -x file ] 如果文件存在且可执行

$$ 
Shell本身的PID（ProcessID） 
$! 
Shell最后运行的后台Process的PID 
$? 
最后运行的命令的结束代码（返回值） 
$- 
使用Set命令设定的Flag一览 
$* 
所有参数列表。如"$*"用「"」括起来的情况、以"$1 $2 … $n"的形式输出所有参数。 
$@ 
所有参数列表。如"$@"用「"」括起来的情况、以"$1" "$2" … "$n" 的形式输出所有参数。 
$# 
添加到Shell的参数个数 
$0 
Shell本身的文件名 
$1～$n 
添加到Shell的各参数值。$1是第1参数、$2是第2参数…。 
...
...

```
### shell重定向
```
类型	               文件描述符	默认情况	      对应文件句柄位置
标准输入（standard input）	0	从键盘获得输入	       /proc/slef/fd/0
标准输出（standard output）	1	输出到屏幕（即控制台）	/proc/slef/fd/1
错误输出（error output）	2	输出到屏幕（即控制台）	/proc/slef/fd/2

>/dev/null  //标准输出1重定向到/dev/null中

2>&1         //错误输出将和标准输出同用一个文件描述符
```

### 函数及其调用
```
函数名()
    {
        命令序列
    }
调用语法
    函数名   参数1   参数2 ....
```

## bash