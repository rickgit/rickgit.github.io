Classfile /H:/workspace/ws-github/rickgit.github.io/javacode/AddMinus.class
  Last modified 2016-3-21; size 286 bytes
  MD5 checksum 14b3afa9e2144a3840d7bd7aa0ee95b8
  Compiled from "AddMinus.java"
public class AddMinus
  SourceFile: "AddMinus.java"
  minor version: 0
  major version: 51
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #4.#14         //  java/lang/Object."<init>":()V
   #2 = Fieldref           #3.#15         //  AddMinus.money:I
   #3 = Class              #16            //  AddMinus
   #4 = Class              #17            //  java/lang/Object
   #5 = Utf8               money
   #6 = Utf8               I
   #7 = Utf8               <init>
   #8 = Utf8               ()V
   #9 = Utf8               Code
  #10 = Utf8               LineNumberTable
  #11 = Utf8               minus
  #12 = Utf8               SourceFile
  #13 = Utf8               AddMinus.java
  #14 = NameAndType        #7:#8          //  "<init>":()V
  #15 = NameAndType        #5:#6          //  money:I
  #16 = Utf8               AddMinus
  #17 = Utf8               java/lang/Object
{
  private volatile int money;
    flags: ACC_PRIVATE, ACC_VOLATILE

  public AddMinus();
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0       
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return        
      LineNumberTable:
        line 1: 0

  public void minus();
    flags: ACC_PUBLIC
    Code:
      stack=4, locals=1, args_size=1
         0: aload_0       
         1: aload_0       
         2: dup           
         3: getfield      #2                  // Field money:I
         6: iconst_1      
         7: iadd          
         8: dup_x1        
         9: putfield      #2                  // Field money:I
        12: putfield      #2                  // Field money:I
        15: return        
      LineNumberTable:
        line 12: 0
        line 18: 15
}
