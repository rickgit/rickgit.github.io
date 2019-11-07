 

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
+-----------------------------------------------------------------------------------------------------+
|  Loops               | loop <label>                                                                 |
+----------------------------------+------------------------------------------------------------------+
|                      |Calling    | esp ebp                                                          |
|                      |Con^ention |                                                                  |
|                      +------------------------------------------------------------------------------+
|                      |           | CMP  JMP	 JE/JZ   JNE/JNZ  JG/JNLE  JGE/JNL   JL/JNGE   JLE/JNG|
|                      |Conditions |                              JA/JNBE  JAE/JNB   JB/JNAE   JBE/JNA|
| Instructions         |           | JXCZ JC    JNC     JO      JNO      JP/JPE   JNP/JPO   JS   JNS  |
|                      +------------------------------------------------------------------------------+
|                      |  Logical  | AND      OR   XOR   TEST   NO                                    |
|                      | Arithmetic| INC      DEC  ADD   SUB    MUL/IMUL      DIV/IDIV                |
|                      +------------------------------------------------------------------------------+
|                      |  Data     |  mov   push        pop     lea                                   |
|                      |  Movement |                                                                  |
+----------------------+-----------+------------------------------------------------------------------+
|                      |  Multiple           |TIMES                    |                              |
|                      |  Initializations    |                         |                              |
|                      |                     |RESB    RESW   RESD      |                              |
|                      |  Allocating Storage |RESQ    REST             |  EQU    %assign   %define    |
| define-directive     |  Uninitialized Data |                         |                              |
|                      |                     |DB    allocates 1 byte   |                              |
|                      |  storage allocation |DW    allocates 2 bytes  |                              |
|                      |  Initialized Data   |DD    allocates 4 bytes  |                              |
|                      |                     |DQ    allocates 8 bytes  |                              |
|                      |                     |DT    allocates 10 bytes |                              |
|                      +----------------------------------------------------------------------------  |
|                      |                 Variables                     |          Constants           |
+----------------------+------------------------------------------------------------------------------+
|  Operands            |  Register addressing                                                         |
|  Addressing Modes    |  Immediate addressing                                                        |
|                      |  Memory addressing                                                           |
+-------------------------------------------+-------------------+-------------------------------------+
|  system call         |  call number in the EAX register                                             |
|(between the user space|  Store the arguments                                                        |
|and the kernel space) |   Call the relevant interrupt (80h)                                          |
|                      |  The result is usually returned in the EAX register                          |
+----------------------+--------------------+---------------------------------------------------------+
|  Memory Segments     |   Data segment     |.data   .bss                                             |
|                      |   Code segment     |.text                                                    |
|                      |   Stack  segment   |                                                         |
+----------------------+--------------------+---------------------------------------------------------+
|                      |                    | Data registers    |  32-bit  EAX,    EBX,   ECX,  EDX   |
|                      |                    |                   |  16-bit   AX,     BX,    CX,   DX   |
|                      |                    +                   |        AH,AL,  BH,BL, CH,CL, DH,DL  |
|                      |  General registers +---------------------------------------------------------+
|                      |                    | Pointer registers |   IP, SP, , BP                      |
|                      |                    +---------------------------------------------------------+
|                      |                    | Index registers   |  16-bit  SI , DI   32-bit  ESI , EDI|
| Processor Registers  +----------------------------------------+-------------------------------------+
|(CPU internal memory) |  Control registers | Flag  :              O  D I T S Z   A   P   C           |
|                      |                    | Bit no: 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 0           |
|                      +------------------------------------------------------------------------------+
|                      |  Segment registers | Code  Data  Stack                                       |
+----------------------+--------------------+---------------------------------------------------------+

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