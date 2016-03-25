reading H:\workspace\ws-github\rickgit.github.io\javacode\JsObjectName.class...
begin classfile
magic: cafebabe
minor_version: 0000
major_version: 0033
constant_pool_count: 0015

constant_pool:
  0001: type{JsObjectName}
  0002: type{java.lang.Object}
  0003: type{java.lang.annotation.Annotation}
  0004: utf8{"name"}
  0005: utf8{"()Ljava/lang/String;"}
  0006: utf8{"AnnotationDefault"}
  0007: utf8{"android"}
  0008: utf8{"SourceFile"}
  0009: utf8{"JsObjectName.java"}
  000a: utf8{"RuntimeVisibleAnnotations"}
  000b: utf8{"Ljava/lang/annotation/Target;"}
  000c: utf8{"value"}
  000d: utf8{"Ljava/lang/annotation/ElementType;"}
  000e: utf8{"TYPE"}
  000f: utf8{"Ljava/lang/annotation/Retention;"}
  0010: utf8{"Ljava/lang/annotation/RetentionPolicy;"}
  0011: utf8{"RUNTIME"}
  0012: utf8{"JsObjectName"}
  0013: utf8{"java/lang/Object"}
  0014: utf8{"java/lang/annotation/Annotation"}
end constant_pool
access_flags: public|interface|abstract|annotation
this_class: type{JsObjectName}
super_class: type{java.lang.Object}
interfaces_count: 0001
interfaces:
  type{java.lang.annotation.Annotation}
fields_count: 0000
methods_count: 0001

methods[0]:
  access_flags: public|abstract
  name: name
  descriptor: ()Ljava/lang/String;
  attributes_count: 0001
  
  attributes[0]:
    name: AnnotationDefault
    length: 00000003
    tag: "s"
    constant_value: "android"
  end attributes[0]
end methods[0]
attributes_count: 0002

attributes[0]:
  name: SourceFile
  length: 00000002
  source: string{"JsObjectName.java"}
end attributes[0]

attributes[1]:
  name: RuntimeVisibleAnnotations
  length: 0000001b
  num_annotations: 0002
  annotations[0]:
    type: java.lang.annotation.Target
    num_elements: 1
    elements[0]:
      element_name: value
      value: 
        tag: "["
        num_values: 1
        element_value[0]:
          tag: "e"
          type_name: Ljava/lang/annotation/ElementType;
          const_name: TYPE
  annotations[1]:
    type: java.lang.annotation.Retention
    num_elements: 1
    elements[0]:
      element_name: value
      value: 
        tag: "e"
        type_name: Ljava/lang/annotation/RetentionPolicy;
        const_name: RUNTIME
end attributes[1]
end classfile
