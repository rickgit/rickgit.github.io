reading AddMinus.class...
begin classfile
magic: cafebabe
minor_version: 0000
major_version: 0033
constant_pool_count: 0032

constant_pool:
  0001: method{java.lang.Object.<init>:()V}
  0002: method{java.lang.System.currentTimeMillis:()J}
  0003: long{0x000000174876e800 / 100000000000}
  0005: field{java.lang.System.out:Ljava/io/PrintStream;}
  0006: type{java.lang.StringBuilder}
  0007: method{java.lang.StringBuilder.<init>:()V}
  0008: string{"===>"}
  0009: method{java.lang.StringBuilder.append:(Ljava/lang/String;)Ljava/lang/St
  ringBuilder;}
  000a: method{java.lang.StringBuilder.append:(J)Ljava/lang/StringBuilder;}
  000b: method{java.lang.StringBuilder.toString:()Ljava/lang/String;}
  000c: method{java.io.PrintStream.println:(Ljava/lang/String;)V}
  000d: type{AddMinus}
  000e: type{java.lang.Object}
  000f: utf8{"<init>"}
  0010: utf8{"()V"}
  0011: utf8{"Code"}
  0012: utf8{"LineNumberTable"}
  0013: utf8{"Add"}
  0014: utf8{"StackMapTable"}
  0015: utf8{"minus"}
  0016: utf8{"SourceFile"}
  0017: utf8{"AddMinus.java"}
  0018: nat{<init>:()V}
  0019: type{java.lang.System}
  001a: nat{currentTimeMillis:()J}
  001b: nat{out:Ljava/io/PrintStream;}
  001c: utf8{"java/lang/StringBuilder"}
  001d: utf8{"===>"}
  001e: nat{append:(Ljava/lang/String;)Ljava/lang/StringBuilder;}
  001f: nat{append:(J)Ljava/lang/StringBuilder;}
  0020: nat{toString:()Ljava/lang/String;}
  0021: type{java.io.PrintStream}
  0022: nat{println:(Ljava/lang/String;)V}
  0023: utf8{"AddMinus"}
  0024: utf8{"java/lang/Object"}
  0025: utf8{"java/lang/System"}
  0026: utf8{"currentTimeMillis"}
  0027: utf8{"()J"}
  0028: utf8{"out"}
  0029: utf8{"Ljava/io/PrintStream;"}
  002a: utf8{"append"}
  002b: utf8{"(Ljava/lang/String;)Ljava/lang/StringBuilder;"}
  002c: utf8{"(J)Ljava/lang/StringBuilder;"}
  002d: utf8{"toString"}
  002e: utf8{"()Ljava/lang/String;"}
  002f: utf8{"java/io/PrintStream"}
  0030: utf8{"println"}
  0031: utf8{"(Ljava/lang/String;)V"}
end constant_pool
access_flags: public|super
this_class: type{AddMinus}
super_class: type{java.lang.Object}
interfaces_count: 0000
fields_count: 0000
methods_count: 0003

methods[0]:
  access_flags: public
  name: <init>
  descriptor: ()V
  attributes_count: 0001
  
  attributes[0]:
    name: Code
    length: 0000001d
    max_stack: 0001
    max_locals: 0001
    code_length: 00000005
    0000: aload_0 // 00
    0001: invokespecial method{java.lang.Object.<init>:()V}
    0004: return
    exception_table_length: 0000
    attributes_count: 0001
    
    attributes[0]:
      name: LineNumberTable
      length: 00000006
      line_number_table_length: 0001
      0000 1
    end attributes[0]
  end attributes[0]
end methods[0]

methods[1]:
  access_flags: public
  name: Add
  descriptor: ()V
  attributes_count: 0001
  
  attributes[0]:
    name: Code
    length: 00000067
    max_stack: 0006
    max_locals: 0005
    code_length: 00000033
    0000: invokestatic method{java.lang.System.currentTimeMillis:()J}
    0003: lstore_1 // 01, category-2
    0004: lconst_0 // +00
    0005: lstore_3 // 03, category-2
    0006: lload_3 // 03, category-2
    0007: ldc2_w #+000000174876e800
    000a: lcmp
    000b: ifge 0015
    000e: lload_3 // 03, category-2
    000f: lconst_1 // +01
    0010: ladd
    0011: lstore_3 // 03, category-2
    0012: goto 0006
    0015: getstatic field{java.lang.System.out:Ljava/io/PrintStream;}
    0018: new type{java.lang.StringBuilder}
    001b: dup
    001c: invokespecial method{java.lang.StringBuilder.<init>:()V}
    001f: ldc string{"===>"}
    0021: invokevirtual method{java.lang.StringBuilder.append:(Ljava/lang/Strin
    g;)Ljava/lang/StringBuilder;}
    0024: invokestatic method{java.lang.System.currentTimeMillis:()J}
    0027: lload_1 // 01, category-2
    0028: lsub
    0029: invokevirtual method{java.lang.StringBuilder.append:(J)Ljava/lang/Str
    ingBuilder;}
    002c: invokevirtual method{java.lang.StringBuilder.toString:()Ljava/lang/St
    ring;}
    002f: invokevirtual method{java.io.PrintStream.println:(Ljava/lang/String;)
    V}
    0032: return
    exception_table_length: 0000
    attributes_count: 0002
    
    attributes[0]:
      name: LineNumberTable
      length: 00000012
      line_number_table_length: 0004
      0000 4
      0004 5
      0015 8
      0032 9
    end attributes[0]
    
    attributes[1]:
      name: StackMapTable
      length: 0000000a
      attribute data
    end attributes[1]
  end attributes[0]
end methods[1]

methods[2]:
  access_flags: public
  name: minus
  descriptor: ()V
  attributes_count: 0001
  
  attributes[0]:
    name: Code
    length: 00000067
    max_stack: 0006
    max_locals: 0005
    code_length: 00000033
    0000: invokestatic method{java.lang.System.currentTimeMillis:()J}
    0003: lstore_1 // 01, category-2
    0004: ldc2_w #+000000174876e800
    0007: lstore_3 // 03, category-2
    0008: lload_3 // 03, category-2
    0009: lconst_0 // +00
    000a: lcmp
    000b: ifle 0015
    000e: lload_3 // 03, category-2
    000f: lconst_1 // +01
    0010: lsub
    0011: lstore_3 // 03, category-2
    0012: goto 0008
    0015: getstatic field{java.lang.System.out:Ljava/io/PrintStream;}
    0018: new type{java.lang.StringBuilder}
    001b: dup
    001c: invokespecial method{java.lang.StringBuilder.<init>:()V}
    001f: ldc string{"===>"}
    0021: invokevirtual method{java.lang.StringBuilder.append:(Ljava/lang/Strin
    g;)Ljava/lang/StringBuilder;}
    0024: invokestatic method{java.lang.System.currentTimeMillis:()J}
    0027: lload_1 // 01, category-2
    0028: lsub
    0029: invokevirtual method{java.lang.StringBuilder.append:(J)Ljava/lang/Str
    ingBuilder;}
    002c: invokevirtual method{java.lang.StringBuilder.toString:()Ljava/lang/St
    ring;}
    002f: invokevirtual method{java.io.PrintStream.println:(Ljava/lang/String;)
    V}
    0032: return
    exception_table_length: 0000
    attributes_count: 0002
    
    attributes[0]:
      name: LineNumberTable
      length: 00000012
      line_number_table_length: 0004
      0000 11
      0004 12
      0015 15
      0032 16
    end attributes[0]
    
    attributes[1]:
      name: StackMapTable
      length: 0000000a
      attribute data
    end attributes[1]
  end attributes[0]
end methods[2]
attributes_count: 0001

attributes[0]:
  name: SourceFile
  length: 00000002
  source: string{"AddMinus.java"}
end attributes[0]
end classfile
