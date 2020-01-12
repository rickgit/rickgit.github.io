 

## 第二代语言

## 环境nasm
[在线编译环境](https://www.tutorialspoint.com/compile_asm_online.php)

```
   Recursion         Macros       File Management
+----------------------+------------------------------------------------------------------------------+
|  Procedures          |  CALL                                                                        |
+-----------------------------------------------------------------------------------------------------+
|  Arrays              |                                                                              |
+-----------------------------------------------------------------------------------------------------+
|  String              | MOVS    LODS    STOS    CMPS   SCAS                                          |
+-----------------------------------------------------------------------------------------------------+
|  Numbers             |                                                                              |
+----------------------------------+------------------------------------------------------------------+
|                      |Calling    | esp ebp                                                          |
|                      |Convention |                                                                  |
|                      +------------------------------------------------------------------------------+
|                      |Loops      | loop (Decrement CX and Loop if CX Not Zero)                      |
|                      +------------------------------------------------------------------------------+
|                      |           | CMP  JMP	 JE/JZ   JNE/JNZ  JG/JNLE  JGE/JNL   JL/JNGE   JLE/JNG|
|                      |Conditions |                              JA/JNBE  JAE/JNB   JB/JNAE   JBE/JNA|
| Instructions         |           | JXCZ JC    JNC     JO      JNO      JP/JPE   JNP/JPO   JS   JNS  |
| (256)                +------------------------------------------------------------------------------+
|                      |  Logical  | AND      OR   XOR   TEST   NO                                    |
|                      | Arithmetic| INC      DEC  ADD   SUB    MUL/IMUL      DIV/IDIV                |
|                      +------------------------------------------------------------------------------+
|                      |  Data     |  mov   push        pop     lea                                   |
|                      |  Movement |                                                                  |
+----------------------+-----------+------------------+----------------+------------------------------+
| Operands  Addressing |  Register addressing           Immediate addressing      Memory addressing   |
| Modes                |                                                                              |
+----------------------+----------------------------+------------+------------------------------------+
|  define-directive    |                            | DB  DW  DD | RESB  RESW RESD|                   |
| (pseudo-opcodes)     | EQU    %assign             | DQ  DT     | RESQ  REST     |                   |
|                      | %define                    | TIMES      |                |                   |
|                      |                            +-----------------------------+                   |
|                      |                            |Data section| BSS section    |    ENTRY          |
|                      +------------------------------------------------------------------------------+
|                      |        Constants           |         Variables           |                   |
+----------------------------------------------------------------+------------------------------------+
|  Memory Segments     | .text                      |   .data    | .bss           |                   |
|                      +-----------------------------------------+------------------------------------+
|                      | Code segment               |   Data segment              |   Stack  segment  |
+---------------------------------------------------+-----------------------------+-------------------+
|  system call         |  call number in the EAX register                                             |
|(between the user space|  Store the arguments                                                        |
|and the kernel space) |   Call the relevant interrupt (80h)                                          |
|                      |  The result is usually returned in the EAX register                          |
+---------------------------------------------------+-----------------------------+---------+---------+
|            |CS (code)|Flag  :              O  D   | 32+bit                      |         |         |
|            |DS (data)|Bit no: 15 14 13 12 11 10   |   EAX,  EBX,  ECX, EDX      |         |16+bit   |
| Processor  |SS (stack)                            | 16+bit                      |IP,      |   SI, DI|
|            |ES,FS    |      I T S Z   A   P   C   |    AX,   BX,   CX,  DX      |SP,      |32+bit   |
| Registers  |,GS      |      9 8 7 6 5 4 3 2 1 0   | AH,AL;BH,BL;CH,CL;DH,DL     |BP       |  ESI,EDI|
|            +----------------------------------------------------------------------------------------+
|(CPU        |         |                            | Data                        |Pointer  |Index    |
| internal   |         |                            | registers                   |registers|registers|
|  memory)   |Segment  |  Control                   +-------------------------------------------------+
|            |registers|  registers                 | General registers                               |
+---------------------------------------------------+-------------------------------------------------+
      dr调试寄存器
     fpu,mmx以及sse寄存器 
```
## 语法
>[Intel 80x86 Assembly Language OpCodes](http://www.mathemainzel.info/files/x86asmref.html)

[i386](https://nju-ics.gitbooks.io/ics2018-programming-assignment/content/i386-intro.html)

```
Instruction General Format
PreFix	OpCode	modRM SIB	Disp	Imm

op（2进制指令）
   Instruction OpCode
mnemonics（op助记符，OpCode 可以对应N个mnemonic；同一个mnemonic 可以对应多个OpCode）
   Instruction syntax

```