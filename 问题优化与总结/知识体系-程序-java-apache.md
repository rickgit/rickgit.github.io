## Commons
[Github Commons](https://github.com/apache?q=commons&type=&language=)

## Commons BCEL
[jvm](http://commons.apache.org/proper/commons-bcel/manual/jvm.html)

![jvm](http://commons.apache.org/proper/commons-bcel/images/classfile.gif)

[The BCEL API](http://commons.apache.org/proper/commons-bcel/manual/bcel-api.html)
```
 001_initial 96ac0670cf9944d4912ab25ec885a4bea651443d Initial revision
        +-----------------------------------------------------------------------------------------------------------------+
        |                 example\Helloify              example\id                    example\HelloWorldBuilder           |
        +---------------------------------------------------------------------+-------------------------------------------+
        |                                                                     |                                           |
        |       ClassParser                    Repository                     |    ClassGen                               |
        |         parse():JavaClass               lookupClass():JavaClass     |      getJavaClass():JavaClass             |
        |                                                                     |                                           |
        +---------------------------------------------------------------------+-------------------------------------------+
        |                                        JavaClass                                                                |
        |                                              dump()                                                             |
        |                                                                                                                 |
        +-----------------------------------------------------------------------------------------------------------------+

```

## commons-beanutils
内省反射
```
  001_component e3ac65ad359c957199b144745c906eac6814337b Initial commit of BeanUtils component.
  002_beanutils c44f209fe457fc6593b09bbec020680665da5443 Initial check-in of BeanUtils component.
* 003_test      c1ea2007d4f6527bd174869603f53841380b5c31 Initial check-in of 'BeanUtils' component.

                +-------------------------------------------------------------------------------------------------+
                |                                   PropertyUtilsTestCase                                         |
                |                                                                                                 |
                +-------------------------------------------------------------------------------------------------+
                |                                                                                                 |
                |                                    PropertyUtils                                                |
                |                                          getPropertyDescriptor():PropertyDescriptor             |
                |                                                                                                 |
                +-------------------------------------------------------------------------------------------------+
                | Introspector                     BeanInfo                                                       |
                |       getBeanInfo():BeanInfo        getPropertyDescriptors():PropertyDescriptor[]               |
                |                                                                                                 |
                |                                                                                                 |
                |                               PropertyDescriptor                                                |
                |                                      getReadMethod():Method                                     |
                |                                      getWriteMethod():Method                                    |
                |                                                                                                 |
                +-------------------------------------------------------------------------------------------------+

```
## commons-bsf
Bean Scripting Framework
脚本构造成Java类，运行时编译加载
[](http://commons.apache.org/proper/commons-bsf/manual.html)

```
  001_lib     5f2ee43dadf5049b78e093f5643bdb6a20e297d7 the local toolkit
* 002_checkin 9911425edd0285e1e6551b5192c6f07378b29d77 first drop checkin, cleanups/adds to follow before cutting release
                        +-------------------------------------------------------------------------------------------------+
                        |                                        Main                                                     |
                        |                                          main()                                                 |
                        +-------------------------------------------------------------------------------------------------+
                        |                                        BSFManager                                               |
                        |                                            compileScript()                                      |
                        |                                            exec()                                               |
                        |                                            eval()                                               |
                        |                                            registeredEngines:Hashtable                          | 
                        |                                                                                                 |
                        |                                      BSFEngineImpl:BSFEngine                                    |
                        |                                            initialize()                                         |
                        |                                            compileScript()                                      |
                        |                                                                                                 |
                        +-------------------------------------------------------------------------------------------------+
                        |    JavaEngine:BSFEngineImpl     JavaClassEngine:BSFEngineImpl   XSLTEngine:BSFEngineImpl   ...  |
                        |                                                                                                 |
                        |                                                                                                 |
                        |                                                                                                 |
                        +-------------------------------------------------------------------------------------------------+
                        |    GeneratedFile                                                                                |
                        |        fos:FileOutputStream                                                                     |
                        |                                                                                                 |
                        |    JavaUtils                                                                                    |
                        |        JDKcompile()                                                                             |
                        |                                                                                                 |
                        |    EngineUtils                                                                                  |
                        |        loadClass():Class                                                                        |
                        |                                                                                                 |
                        +-------------------------------------------------------------------------------------------------+

```

### commons-chain
[five key interfaces: Context,Command,Chain,Filter,Catalog](http://commons.apache.org/proper/commons-chain/cookbook.html)
```
* 001_initial d1dce747950bd3f79fd8fa1fdaeadc37ecfab373 Initial revision
              +-------------------------------------------------------------------------------------------------+
              |                            ChainBaseTestCase                                                    |
              |                                    chain:ChainBase                                              |
              |                                    context:ContextBase                                          |
              |                                                                                                 |
              +-------------------------------------------------------------------------------------------------+
              |         ChainBase:Chain                               ContextBase:Context                       |
              |             addCommand()                                    attributes:Map                      |
              |             commands:Command[]                                                                  |
              |             execute(context:Context)                                                            |
              |                                                                                                 |
              |         Command                                                                                 |
              |               execute(context)                                                                  |
              |         Filter:Command                                                                          |
              |               postprocess(context,saveException)                                                |
              |                                                                                                 |
              |                                                                                                 |
              +-------------------------------------------------------------------------------------------------+

```

## commons-cli
[  Getting started ](http://commons.apache.org/proper/commons-cli/introduction.html)
[usage](http://commons.apache.org/proper/commons-cli/usage.html)
```
* 001_initial aae50c585ec3ac33c6a9af792e80378904a73195 moved cli over from the sandbox to commons proper
              +-------------------------------------------------------------------------------------------------+
              |                                       ParseTest                                                 |
              |                                           _options:Options                                      |
              |                                                                                                 |
              +-------------------------------------------------------------------------------------------------+
              |                                       Options                          Option                   |
              |                                           parse():CommandLine              opt:Char             |
              |                                           addOption():Options              longOpt:Str          |
              |                                           shortOpts:Map                    hasArg:boolean       |
              |                                           longOpts:Map                     description:Str      |
              |                                           requiredOpts:Map                 required:boolean     |
              |                                           processOption()                  multipleArgs:boolean |
              |                                           checkRequiredOptions()           type:Object          |
              |                                                                            values:ArrayList     |
              |                                    CommandLine                                                  |
              |                                          options:Map                                            |
              |                                          types:Map                                              |
              |                                          setOpt(option:Option)                                  |
              |                                          args:List//unrecognised                                |
              |                                          addArg()                                               |
              |                                          getOptionValue()                                       |
              |                                                                                                 |
              +-------------------------------------------------------------------------------------------------+

```


## commons-codec
[userguide](http://commons.apache.org/proper/commons-codec/userguide.html) 
Binary Encoders | Digest Encoders | Language Encoders | Network Encoders
```
  001_initial 71205dde6b8eda4140c777b4fb71935d73bf2d12 Added Codec to commons from the sandbox
* 113_1.13    beafa49f88be397f89b78d125d2c7c52b0114006 Fix the site's source repository link.
                    \---codec
                        |   BinaryDecoder.java
                        |   BinaryEncoder.java
                        |   CharEncoding.java
                        |   Charsets.java
                        |   Decoder.java
                        |   DecoderException.java
                        |   Encoder.java
                        |   EncoderException.java
                        |   package.html
                        |   Resources.java
                        |   StringDecoder.java
                        |   StringEncoder.java
                        |   StringEncoderComparator.java
                        |
                        +---binary
                        |       Base32.java
                        |       Base32InputStream.java
                        |       Base32OutputStream.java
                        |       Base64.java
                        |       Base64InputStream.java
                        |       Base64OutputStream.java
                        |       BaseNCodec.java
                        |       BaseNCodecInputStream.java
                        |       BaseNCodecOutputStream.java
                        |       BinaryCodec.java
                        |       CharSequenceUtils.java
                        |       Hex.java
                        |       package.html
                        |       StringUtils.java
                        |
                        +---cli
                        |       Digest.java
                        |
                        +---digest
                        |       B64.java
                        |       Crypt.java
                        |       DigestUtils.java
                        |       HmacAlgorithms.java
                        |       HmacUtils.java
                        |       Md5Crypt.java
                        |       MessageDigestAlgorithms.java
                        |       MurmurHash2.java
                        |       MurmurHash3.java
                        |       package.html
                        |       PureJavaCrc32.java
                        |       PureJavaCrc32C.java
                        |       Sha2Crypt.java
                        |       UnixCrypt.java
                        |       XXHash32.java
                        |
                        +---language
                        |   |   AbstractCaverphone.java
                        |   |   Caverphone.java
                        |   |   Caverphone1.java
                        |   |   Caverphone2.java
                        |   |   ColognePhonetic.java
                        |   |   DaitchMokotoffSoundex.java
                        |   |   DoubleMetaphone.java
                        |   |   MatchRatingApproachEncoder.java
                        |   |   Metaphone.java
                        |   |   Nysiis.java
                        |   |   package.html
                        |   |   RefinedSoundex.java
                        |   |   Soundex.java
                        |   |   SoundexUtils.java
                        |   |
                        |   \---bm
                        |           BeiderMorseEncoder.java
                        |           Lang.java
                        |           Languages.java
                        |           NameType.java
                        |           package.html
                        |           PhoneticEngine.java
                        |           ResourceConstants.java
                        |           Rule.java
                        |           RuleType.java
                        |
                        \---net
                                BCodec.java
                                package.html
                                PercentCodec.java
                                QCodec.java
                                QuotedPrintableCodec.java
                                RFC1522Codec.java
                                URLCodec.java
                                Utils.java
```

## commons-collections
[overview](http://commons.apache.org/proper/commons-collections/index.html)

```
  001_initial 13a119de0b5709d50214458482f249f5de2fd4b6 Initial revision
* 440_v4.4    cab58b3a8093a2f6b84f12783a3fb358747310f7 Update POM version numbers for Apache Commons Collections release 4.4

```

## commons-compress
The compressor formats supported are gzip, bzip2, xz, lzma, Pack200, DEFLATE, Brotli, DEFLATE64, ZStandard and Z, the archiver formats are 7z, ar, arj, cpio, dump, tar and zip. Pack200 is a special case as it can only compress JAR files.

```

```

## commons-configuration
Properties files
XML documents
Windows INI files
Property list files (plist)
JNDI
JDBC Datasource
System properties
Applet parameters
Servlet parameters
[](http://commons.apache.org/proper/commons-configuration/userguide/quick_start.html)
```

```


## commons-crypto（封装 openssl和jce）
random	,cipher	,stream	

## commons-cvs


## commons-daemon

服务器 System.exit(int) 不安全；
采用 daemons 关闭；（Linux的信号，Windows应用自带的方法）
- 以daemon方式运行tomcat可使tomcat不受终端影响，不会因为退出终端而停止运行。
- 可以让tomcat以普通用户身份运行。
- 可以让tomcat在系统启动时自动运行。
```
* 001_initial 49cbb142a2b5d7d89aab077dc63f7646828c9408 Initial revision
              commons-daemon/src/docs/daemon.html
              +----------------------------------------------------------------------------------------------------------+
              |                                     ServiceDaemon.sh                                                     |
              |                                                                                                          |
              +----------------------------------------------------------------------------------------------------------+
              |                                          jsvc-unix.c                                                     |
              |                                             main()//fork() create deamon                                 |
              |                                                                                                          |
              |                                          java_load.c:java.h                                              |
              |                                             ja^a_library(arg_data *args, home_data *data):char *         |
              |                                             ja^a_init(arg_data *args, home_data *data):boolean           |
              |                                             java_destroy(int exit):bool                                  |
              |                                             java_load(arg_data *args):bool                               |
              |                                             java_start(void):bool                                        |
              |                                             java_stop(void):bool                                         |
              |                                             ja^a_version(void):bool                                      |
              |                                             java_check(arg_data *args):bool                              |
              |                                             LOADER:"org/apache/commons/daemon/support/DaemonLoader"      |
              +----------------------------------------------------------------------------------------------------------+
              |                                                                                                          |
              |                                          DaemonLoader                                                    |
              +----------------------------------------------------------------------------------------------------------+
              |                                          ServiceDaemon:Deamon                                            |
              |                                                                                                          |
              |                                                                                                          |
              +----------------------------------------------------------------------------------------------------------+

```

## commons-dbcp
[guide](http://commons.apache.org/proper/commons-dbcp/guide/index.html)

```
* 001_initial 2759abae42100a486dda09e787d5aee13a9ba1f9 Initial revision
              doc\README.txt
              +------------------------------------------------------------+--------------------------------------+
              |                                                            |                                      |
              |     ManualPoolingDriverExample     ManualPoolingDataSource |          JOCLPoolingDriverExample    |
              |                                                            |                                      |
              +------------------------------------------------------------+                                      |
              |     PoolingDriver                   PoolingDataSource      |                                      |
              |         registerPool()                      :DataSource    |                                      |
              |     DriverManager                       getConnection()    |                                      |
              |         getConnection()                                    |                                      |
              +------------------------------------------------------------+--------------------------------------+
              |     GenericObjectPool:ObjectPool                                                                  | 
              |                                                            |                                      |
              |     PoolableConnectionFactory                              |  poolingDriverExample.jocl.sample    | 
              |                                                            |                                      |
              |     DriverManagerConnectionFactory                         |                                      |
              |               :ConnectionFactory                           |                                      |
              |                                                            |                                      |
              +------------------------------------------------------------+--------------------------------------+
              |                                   PoolingDriver:Driver                                            |
              |                                         _pools:HashMap                                            |
              |                                         getPool(name:Str)//from jocl file                         |
              +------------------------------------------------------------+  JOCLContentHandler                  |
              | [commons-pool]        ObjectPool                           |          parse():JOCLContentHandler  |
              |                            borrowObject():Connection       |          getValue():Connection       |
              |                                                            +--------------------------------------+
              |                                                                                                   |
              +----------------------------------------------------------------------------------------------------

  270_v2.7    9b3f4f9bbff1be4afd992f68a388faf9b8cbf907 Update POM version numbers for Apache Commons DBCP release 2.7.0


```

## commons-dbutils
[example](http://commons.apache.org/proper/commons-dbutils/examples.html)
桥接模式，解耦
```
* 001_initial 0342b92678f201c01deaca416067a62f53b1fc22 Move DbUtils from sandbox to commons proper.

              +---------------------------------------------------------------------------------------------------+
              |                                      QueryRunner                                                  |
              |                                            query(sql:String ,param:Object,rsh:ResultSetHandler)   |
              |                                                                                                   |
              |                                                                                                   |
              |                                         ResultSetHandler<T>                                       |
              |                                             handle(rs:ResultSet ):T                               |
              |                                                                                                   |
              |                                                                                                   |
              |                                                                                                   |
              +----------------------------------------------------------------------------------------------------
```


## commons-digester
优化sax，屏蔽xml解析过程
```
* 001_initial 88c2ecccc5d0c5272c9e5b6af88888b71bad633e Check in the Commons digester module, transferred from the sandbo
            src\java\org\apache\commons\digester\package.html
            +---------------------------------------------------------------------------------------------------+
            |                                      Digester:org.xml.sax.HandlerBase                             |
            |                                         addObjectCreate()                                         |
            |                                         rules:Map<Str,List<Rule> >                                |
            |                                         addObjectCreate(pattern:Str,classname:Str)                |
            |                                         addCallMethod()                                           |
            |                                         parse()                                                   |
            +---------------------------------------------------------------------------------------------------+
            |                                                                                                   |
            |  ObjectCreateRule  CallMethodRule  CallParamRule    SetNextRule   SetPropertiesRule    SetTopRule |
            |                                                                                                   |
            |                                                                   SetPropertyRule                 |
            |                                                                                                   |
            +----------------------------------------------------------------------------------------------------

```

## commons-email
[userguide](http://commons.apache.org/proper/commons-email/userguide.html)

```
* 001_initial d42354dcec209f0aaba4963ed4f84e6d7768076c Initial revision
              +---------------------------------------------------------------------------------------------------+
              |  SimpleEmail:Email                        MultiPartEmail:Email         HtmlEmail:MultiPartEmail   |
              |         setHostName()                         attach(attach:EmailAttachment)                      |
              |         setSmtpPort()                         boolHasAttachments:boolean                          |
              |         setAuthenticator(auth:authenticator)  container:MimeMultipart                             |
              |         setSSLOnConnect()                                                                         |
              |         setFrom()                                                                                 |
              |         setSubject()                                                                              |
              |         setMsg()                                                                                  |
              |         addTo()                                                                                   |
              +---------------------------------------------------------------------------------------------------+
              |  DefaultAuthenticator:Authenticator       EmailAttachment                                         |
              |                                                  setPath()                                        |
              |                                                  setURL()                                         |
              |                                                  setDisposition()                                 |
              |                                                  setDescription()                                 |
              |                                                  setName()                                        |
              |                                                                                                   |
              +---------------------------------------------------------------------------------------------------+
              |                                       Email                                                       |
              |                                         getMailSession()                                          |
              |                                         send()                                                    |
              +---------------------------------------------------------------------------------------------------+
              | [javax.mail]                                                                                      |
              |                          Transport                                                                |
              |                              send(msg:MimeMessage)                                                |
              |                                              MimeMultipart                                        |
              |                                                   addBodyPart(mbp:MimeBodyPart)                   |
              |                                                                                                   |
              |                                              MimeBodyPart                                         |
              |                                                   setDataHandler(data:DataHandler)                |
              |                                                                                                   |
              |                                              DataHandler                                          |
              |                                                  ds:DataSource                                    |
              |                                                                                                   |
              +---------------------------------------------------------------------------------------------------+
  002_test    3ee78d59c3dbb4fee3adf917cf838380c3ed9024 Somehow missed all test code when importing

```

## commons-exec
兼容各个系统或平台的脚本执行
[tutorial](http://commons.apache.org/proper/commons-exec/tutorial.html)
```
  001_initial     0f2fab2869fc98c1bfa5404747b6e87b870fe422 initial commit
* 010_commandline 94b6fada1badb00a6aec9cd13b9d9aac28fd7eda Submitted by: Niklas Gustavsson Reviewed by:  Brett Porter A copy of several classes from the Apache Ant codebase, restructured into
a reusable library without dependence on Ant itself.
                  +----------------------------------------------------------------------------------------+
                  |                                  Execute                                               |
                  |                                     runCommand(cmdline:CommandLine)                    |
                  |                                     setCommandline(cmdline:CommandLine )               |
                  |                                     execute()                                          |
                  |                                                                                        |
                  |       CommandLineImpl:CommandLine                            CommandLauncher           |
                  |            cmdl:CommandLine                                         exec()             |
                  |            getCommandline():String[]                                                   |
                  |                                                                                        |
                  |                                                         Runtime                        |
                  |                                                             getRuntime()               |
                  |                                                             exec()                     |
                  +----------------------------------------------------------------------------------------+

```

## commons-uploadfile
解析文件上传
```
* 002_initial d423dbce722380807f3c6cc26e5c3758937b0234 Html file upload utility, decoupled from Turbine.
              +----------------------------------------------------------------------------------------+
              |                                   FileUpload                                           |
              |                                        parseRequest(req:HttpServletRequest )           |
              |                                                                                        |
              |                                                                                        |
              |                                   HttpServletRequest                                   |
              |                                        getInputStream():InputStream                    |
              |                                                                                        |
              +----------------------------------------------------------------------------------------+
```

## commons-functor
Predicates, Functions, Procedures, Generators, Ranges , Aggregators