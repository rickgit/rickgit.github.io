Classfile /H:/workspace/ws-github/rickgit.github.io/javacode/AddMinus.class
  Last modified 2016-3-18; size 826 bytes
  MD5 checksum 59bf29e955fc465b75b21ba7dfddbf92
  Compiled from "AddMinus.java"
public class AddMinus
  SourceFile: "AddMinus.java"
  minor version: 0
  major version: 51
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #14.#24        //  java/lang/Object."<init>":()V
   #2 = Methodref          #25.#26        //  java/lang/System.currentTimeMillis:()J
   #3 = Long               100000000000l
   #5 = Fieldref           #25.#27        //  java/lang/System.out:Ljava/io/PrintStream;
   #6 = Class              #28            //  java/lang/StringBuilder
   #7 = Methodref          #6.#24         //  java/lang/StringBuilder."<init>":()V
   #8 = String             #29            //  ===>
   #9 = Methodref          #6.#30         //  java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
  #10 = Methodref          #6.#31         //  java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
  #11 = Methodref          #6.#32         //  java/lang/StringBuilder.toString:()Ljava/lang/String;
  #12 = Methodref          #33.#34        //  java/io/PrintStream.println:(Ljava/lang/String;)V
  #13 = Class              #35            //  AddMinus
  #14 = Class              #36            //  java/lang/Object
  #15 = Utf8               <init>
  #16 = Utf8               ()V
  #17 = Utf8               Code
  #18 = Utf8               LineNumberTable
  #19 = Utf8               Add
  #20 = Utf8               StackMapTable
  #21 = Utf8               minus
  #22 = Utf8               SourceFile
  #23 = Utf8               AddMinus.java
  #24 = NameAndType        #15:#16        //  "<init>":()V
  #25 = Class              #37            //  java/lang/System
  #26 = NameAndType        #38:#39        //  currentTimeMillis:()J
  #27 = NameAndType        #40:#41        //  out:Ljava/io/PrintStream;
  #28 = Utf8               java/lang/StringBuilder
  #29 = Utf8               ===>
  #30 = NameAndType        #42:#43        //  append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
  #31 = NameAndType        #42:#44        //  append:(J)Ljava/lang/StringBuilder;
  #32 = NameAndType        #45:#46        //  toString:()Ljava/lang/String;
  #33 = Class              #47            //  java/io/PrintStream
  #34 = NameAndType        #48:#49        //  println:(Ljava/lang/String;)V
  #35 = Utf8               AddMinus
  #36 = Utf8               java/lang/Object
  #37 = Utf8               java/lang/System
  #38 = Utf8               currentTimeMillis
  #39 = Utf8               ()J
  #40 = Utf8               out
  #41 = Utf8               Ljava/io/PrintStream;
  #42 = Utf8               append
  #43 = Utf8               (Ljava/lang/String;)Ljava/lang/StringBuilder;
  #44 = Utf8               (J)Ljava/lang/StringBuilder;
  #45 = Utf8               toString
  #46 = Utf8               ()Ljava/lang/String;
  #47 = Utf8               java/io/PrintStream
  #48 = Utf8               println
  #49 = Utf8               (Ljava/lang/String;)V
{
  public AddMinus();
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0       
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return        
      LineNumberTable:
        line 1: 0

  public void Add();
    flags: ACC_PUBLIC
    Code:
      stack=6, locals=5, args_size=1
         0: invokestatic  #2                  // Method java/lang/System.currentTimeMillis:()J
         3: lstore_1      
         4: lconst_0      
         5: lstore_3      
         6: lload_3       
         7: ldc2_w        #3                  // long 100000000000l
        10: lcmp          
        11: ifge          21
        14: lload_3       
        15: lconst_1      
        16: ladd          
        17: lstore_3      
        18: goto          6
        21: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;
        24: new           #6                  // class java/lang/StringBuilder
        27: dup           
        28: invokespecial #7                  // Method java/lang/StringBuilder."<init>":()V
        31: ldc           #8                  // String ===>
        33: invokevirtual #9                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        36: invokestatic  #2                  // Method java/lang/System.currentTimeMillis:()J
        39: lload_1       
        40: lsub          
        41: invokevirtual #10                 // Method java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        44: invokevirtual #11                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        47: invokevirtual #12                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        50: return        
      LineNumberTable:
        line 4: 0
        line 5: 4
        line 8: 21
        line 9: 50
      StackMapTable: number_of_entries = 2
           frame_type = 253 /* append */
             offset_delta = 6
        locals = [ long, long ]
           frame_type = 250 /* chop */
          offset_delta = 14


  public void minus();
    flags: ACC_PUBLIC
    Code:
      stack=6, locals=5, args_size=1
         0: invokestatic  #2                  // Method java/lang/System.currentTimeMillis:()J
         3: lstore_1      
         4: ldc2_w        #3                  // long 100000000000l
         7: lstore_3      
         8: lload_3       
         9: lconst_0      
        10: lcmp          
        11: ifle          21
        14: lload_3       
        15: lconst_1      
        16: lsub          
        17: lstore_3      
        18: goto          8
        21: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;
        24: new           #6                  // class java/lang/StringBuilder
        27: dup           
        28: invokespecial #7                  // Method java/lang/StringBuilder."<init>":()V
        31: ldc           #8                  // String ===>
        33: invokevirtual #9                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        36: invokestatic  #2                  // Method java/lang/System.currentTimeMillis:()J
        39: lload_1       
        40: lsub          
        41: invokevirtual #10                 // Method java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        44: invokevirtual #11                 // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        47: invokevirtual #12                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
        50: return        
      LineNumberTable:
        line 11: 0
        line 12: 4
        line 15: 21
        line 16: 50
      StackMapTable: number_of_entries = 2
           frame_type = 253 /* append */
             offset_delta = 8
        locals = [ long, long ]
           frame_type = 250 /* chop */
          offset_delta = 12

}
