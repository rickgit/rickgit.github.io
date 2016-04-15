reading H:\workspace\ws-github\rickgit.github.io\javacode\AddMinus.class...
begin classfile
magic: cafebabe
minor_version: 0000
major_version: 0033
constant_pool_count: 0047

constant_pool:
  0001: method{java.lang.Object.<init>:()V}
  0002: field{AddMinus.money:I}
  0003: field{java.lang.System.out:Ljava/io/PrintStream;}
  0004: type{java.lang.StringBuilder}
  0005: method{java.lang.StringBuilder.<init>:()V}
  0006: string{"a"}
  0007: method{java.lang.StringBuilder.append:(Ljava/lang/String;)Ljava/lang/St
  ringBuilder;}
  0008: method{java.lang.StringBuilder.append:(I)Ljava/lang/StringBuilder;}
  0009: method{java.lang.StringBuilder.toString:()Ljava/lang/String;}
  000a: method{java.io.PrintStream.println:(Ljava/lang/String;)V}
  000b: method{java.lang.String.hashCode:()I}
  000c: string{"0"}
  000d: method{java.lang.String.equals:(Ljava/lang/Object;)Z}
  000e: string{"wrwre1"}
  000f: string{"123444"}
  0010: string{"4"}
  0011: type{AddMinus}
  0012: type{java.lang.Object}
  0013: utf8{"money"}
  0014: utf8{"I"}
  0015: utf8{"<init>"}
  0016: utf8{"()V"}
  0017: utf8{"Code"}
  0018: utf8{"LineNumberTable"}
  0019: utf8{"minus"}
  001a: utf8{"testPlusPlus"}
  001b: utf8{"StackMapTable"}
  001c: utf8{"simpleSwitch"}
  001d: utf8{"(I)I"}
  001e: utf8{"simpleSwitchString"}
  001f: utf8{"(Ljava/lang/String;)I"}
  0020: type{java.lang.String}
  0021: utf8{"SourceFile"}
  0022: utf8{"AddMinus.java"}
  0023: nat{<init>:()V}
  0024: nat{money:I}
  0025: type{java.lang.System}
  0026: nat{out:Ljava/io/PrintStream;}
  0027: utf8{"java/lang/StringBuilder"}
  0028: utf8{"a"}
  0029: nat{append:(Ljava/lang/String;)Ljava/lang/StringBuilder;}
  002a: nat{append:(I)Ljava/lang/StringBuilder;}
  002b: nat{toString:()Ljava/lang/String;}
  002c: type{java.io.PrintStream}
  002d: nat{println:(Ljava/lang/String;)V}
  002e: type{java.lang.String}
  002f: nat{hashCode:()I}
  0030: utf8{"0"}
  0031: nat{equals:(Ljava/lang/Object;)Z}
  0032: utf8{"wrwre1"}
  0033: utf8{"123444"}
  0034: utf8{"4"}
  0035: utf8{"AddMinus"}
  0036: utf8{"java/lang/Object"}
  0037: utf8{"java/lang/String"}
  0038: utf8{"java/lang/System"}
  0039: utf8{"out"}
  003a: utf8{"Ljava/io/PrintStream;"}
  003b: utf8{"append"}
  003c: utf8{"(Ljava/lang/String;)Ljava/lang/StringBuilder;"}
  003d: utf8{"(I)Ljava/lang/StringBuilder;"}
  003e: utf8{"toString"}
  003f: utf8{"()Ljava/lang/String;"}
  0040: utf8{"java/io/PrintStream"}
  0041: utf8{"println"}
  0042: utf8{"(Ljava/lang/String;)V"}
  0043: utf8{"hashCode"}
  0044: utf8{"()I"}
  0045: utf8{"equals"}
  0046: utf8{"(Ljava/lang/Object;)Z"}
end constant_pool
access_flags: public|super
this_class: type{AddMinus}
super_class: type{java.lang.Object}
interfaces_count: 0000
fields_count: 0001

fields[0]:
  access_flags: private|volatile
  name: money
  descriptor: I
  attributes_count: 0000
end fields[0]
methods_count: 0005

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
  name: minus
  descriptor: ()V
  attributes_count: 0001
  
  attributes[0]:
    name: Code
    length: 0000002c
    max_stack: 0004
    max_locals: 0001
    code_length: 00000010
    0000: aload_0 // 00
    0001: aload_0 // 00
    0002: dup
    0003: getfield field{AddMinus.money:I}
    0006: iconst_1 // #+01
    0007: iadd
    0008: dup_x1
    0009: putfield field{AddMinus.money:I}
    000c: putfield field{AddMinus.money:I}
    000f: return
    exception_table_length: 0000
    attributes_count: 0001
    
    attributes[0]:
      name: LineNumberTable
      length: 0000000a
      line_number_table_length: 0002
      0000 12
      000f 18
    end attributes[0]
  end attributes[0]
end methods[1]

methods[2]:
  access_flags: public
  name: testPlusPlus
  descriptor: ()V
  attributes_count: 0001
  
  attributes[0]:
    name: Code
    length: 000000a5
    max_stack: 0003
    max_locals: 0002
    code_length: 0000005e
    0000: iconst_0 // #+00
    0001: istore_1 // 01
    0002: iinc 01, #+01
    0005: iinc 01, #+01
    0008: iinc 01, #+01
    000b: iload_1 // 01
    000c: iconst_4 // #+04
    000d: if_icmpge 002c
    0010: getstatic field{java.lang.System.out:Ljava/io/PrintStream;}
    0013: new type{java.lang.StringBuilder}
    0016: dup
    0017: invokespecial method{java.lang.StringBuilder.<init>:()V}
    001a: ldc string{"a"}
    001c: invokevirtual method{java.lang.StringBuilder.append:(Ljava/lang/Strin
    g;)Ljava/lang/StringBuilder;}
    001f: iload_1 // 01
    0020: invokevirtual method{java.lang.StringBuilder.append:(I)Ljava/lang/Str
    ingBuilder;}
    0023: invokevirtual method{java.lang.StringBuilder.toString:()Ljava/lang/St
    ring;}
    0026: invokevirtual method{java.io.PrintStream.println:(Ljava/lang/String;)
    V}
    0029: goto 0008
    002c: iload_1 // 01
    002d: iinc 01, #+01
    0030: bipush #+06
    0032: if_icmpge 0051
    0035: getstatic field{java.lang.System.out:Ljava/io/PrintStream;}
    0038: new type{java.lang.StringBuilder}
    003b: dup
    003c: invokespecial method{java.lang.StringBuilder.<init>:()V}
    003f: ldc string{"a"}
    0041: invokevirtual method{java.lang.StringBuilder.append:(Ljava/lang/Strin
    g;)Ljava/lang/StringBuilder;}
    0044: iload_1 // 01
    0045: invokevirtual method{java.lang.StringBuilder.append:(I)Ljava/lang/Str
    ingBuilder;}
    0048: invokevirtual method{java.lang.StringBuilder.toString:()Ljava/lang/St
    ring;}
    004b: invokevirtual method{java.io.PrintStream.println:(Ljava/lang/String;)
    V}
    004e: goto 002c
    0051: iload_1 // 01
    0052: iinc 01, #+01
    0055: bipush #+08
    0057: if_icmpge 005d
    005a: goto 0051
    005d: return
    exception_table_length: 0000
    attributes_count: 0002
    
    attributes[0]:
      name: LineNumberTable
      length: 00000026
      line_number_table_length: 0009
      0000 20
      0002 21
      0005 22
      0008 23
      0010 24
      002c 26
      0035 27
      0051 29
      005d 31
    end attributes[0]
    
    attributes[1]:
      name: StackMapTable
      length: 00000009
      attribute data
    end attributes[1]
  end attributes[0]
end methods[2]

methods[3]:
  access_flags: public
  name: simpleSwitch
  descriptor: (I)I
  attributes_count: 0001
  
  attributes[0]:
    name: Code
    length: 00000060
    max_stack: 0001
    max_locals: 0002
    code_length: 0000002c
    0000: iload_1 // 01
    0001: tableswitch
      +00000000: 0024
      +00000001: 0026
      +00000004: 0028
      default: 002a
    0024: iconst_3 // #+03
    0025: ireturn
    0026: iconst_2 // #+02
    0027: ireturn
    0028: iconst_1 // #+01
    0029: ireturn
    002a: iconst_m1 // #-01
    002b: ireturn
    exception_table_length: 0000
    attributes_count: 0002
    
    attributes[0]:
      name: LineNumberTable
      length: 00000016
      line_number_table_length: 0005
      0000 33
      0024 35
      0026 37
      0028 39
      002a 41
    end attributes[0]
    
    attributes[1]:
      name: StackMapTable
      length: 00000006
      attribute data
    end attributes[1]
  end attributes[0]
end methods[3]

methods[4]:
  access_flags: public
  name: simpleSwitchString
  descriptor: (Ljava/lang/String;)I
  attributes_count: 0001
  
  attributes[0]:
    name: Code
    length: 000000d6
    max_stack: 0002
    max_locals: 0004
    code_length: 00000092
    0000: aload_1 // 01
    0001: astore_2 // 02
    0002: iconst_m1 // #-01
    0003: istore_3 // 03
    0004: aload_2 // 02
    0005: invokevirtual method{java.lang.String.hashCode:()I}
    0008: lookupswitch
      -2e71061e: 0042
      +00000030: 0034
      +00000034: 005e
      +56760642: 0050
      default: 0069
    0034: aload_2 // 02
    0035: ldc string{"0"}
    0037: invokevirtual method{java.lang.String.equals:(Ljava/lang/Object;)Z}
    003a: ifeq 0069
    003d: iconst_0 // #+00
    003e: istore_3 // 03
    003f: goto 0069
    0042: aload_2 // 02
    0043: ldc string{"wrwre1"}
    0045: invokevirtual method{java.lang.String.equals:(Ljava/lang/Object;)Z}
    0048: ifeq 0069
    004b: iconst_1 // #+01
    004c: istore_3 // 03
    004d: goto 0069
    0050: aload_2 // 02
    0051: ldc string{"123444"}
    0053: invokevirtual method{java.lang.String.equals:(Ljava/lang/Object;)Z}
    0056: ifeq 0069
    0059: iconst_2 // #+02
    005a: istore_3 // 03
    005b: goto 0069
    005e: aload_2 // 02
    005f: ldc string{"4"}
    0061: invokevirtual method{java.lang.String.equals:(Ljava/lang/Object;)Z}
    0064: ifeq 0069
    0067: iconst_3 // #+03
    0068: istore_3 // 03
    0069: iload_3 // 03
    006a: tableswitch
      +00000000: 0088
      +00000001: 008a
      +00000002: 008c
      +00000003: 008e
      default: 0090
    0088: iconst_3 // #+03
    0089: ireturn
    008a: iconst_2 // #+02
    008b: ireturn
    008c: iconst_2 // #+02
    008d: ireturn
    008e: iconst_1 // #+01
    008f: ireturn
    0090: iconst_m1 // #-01
    0091: ireturn
    exception_table_length: 0000
    attributes_count: 0002
    
    attributes[0]:
      name: LineNumberTable
      length: 0000001a
      line_number_table_length: 0006
      0000 45
      0088 47
      008a 49
      008c 51
      008e 53
      0090 55
    end attributes[0]
    
    attributes[1]:
      name: StackMapTable
      length: 00000012
      attribute data
    end attributes[1]
  end attributes[0]
end methods[4]
attributes_count: 0001

attributes[0]:
  name: SourceFile
  length: 00000002
  source: string{"AddMinus.java"}
end attributes[0]
end classfile
