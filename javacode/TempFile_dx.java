reading H:\workspace\ws-github\rickgit.github.io\javacode\AddMinus.class...
begin classfile
magic: cafebabe
minor_version: 0000
major_version: 0033
constant_pool_count: 0012

constant_pool:
  0001: method{java.lang.Object.<init>:()V}
  0002: field{AddMinus.money:I}
  0003: type{AddMinus}
  0004: type{java.lang.Object}
  0005: utf8{"money"}
  0006: utf8{"I"}
  0007: utf8{"<init>"}
  0008: utf8{"()V"}
  0009: utf8{"Code"}
  000a: utf8{"LineNumberTable"}
  000b: utf8{"minus"}
  000c: utf8{"SourceFile"}
  000d: utf8{"AddMinus.java"}
  000e: nat{<init>:()V}
  000f: nat{money:I}
  0010: utf8{"AddMinus"}
  0011: utf8{"java/lang/Object"}
end constant_pool
access_flags: public|super
this_class: type{AddMinus}
super_class: type{java.lang.Object}
interfaces_count: 0000
fields_count: 0001

fields[0]:
  access_flags: private
  name: money
  descriptor: I
  attributes_count: 0000
end fields[0]
methods_count: 0002

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
attributes_count: 0001

attributes[0]:
  name: SourceFile
  length: 00000002
  source: string{"AddMinus.java"}
end attributes[0]
end classfile
