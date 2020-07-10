## 糟糕的翻译
socket 套接字（信口 a device on a piece of electrical equipment that you can fix a plug, alight BULB, etc. into）
context 上下文（the situation within which something exists or happens, and that can help explain it）
handle 句柄(control ,to control a vehicle, an animal, a tool, etc)
macro宏
## RFC
```http


                  0      7 8     15 16    23 24    31
                 +--------+--------+--------+--------+
                 |     Source      |   Destination   |
                 |      Port       |      Port       |
                 +--------+--------+--------+--------+
                 |                 |                 |
                 |     Length      |    Checksum     |
                 +--------+--------+--------+--------+
                 |
                 |          data octets ...
                 +---------------- ...

                      User Datagram Header Format




    0                   1                   2                   3
    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |          Source Port          |       Destination Port        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                        Sequence Number                        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Acknowledgment Number                      |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |  Data |           |U|A|P|R|S|F|                               |
   | Offset| Reserved  |R|C|S|S|Y|I|            Window             |
   |       |           |G|K|H|T|N|N|                               |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |           Checksum            |         Urgent Pointer        |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                    Options                    |    Padding    |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
   |                             data                              |
   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

                            TCP Header Format
面向连接，可靠的（seq，ack num），字节流（滑动窗口）


      TCP A                                                TCP B

  1.  CLOSED                                               LISTEN

  2.  SYN-SENT    --> <SEQ=100><CTL=SYN>               --> SYN-RECEIVED

  3.  ESTABLISHED <-- <SEQ=300><ACK=101><CTL=SYN,ACK>  <-- SYN-RECEIVED

  4.  ESTABLISHED --> <SEQ=101><ACK=301><CTL=ACK>       --> ESTABLISHED

  5.  ESTABLISHED --> <SEQ=101><ACK=301><CTL=ACK><DATA> --> ESTABLISHED

          Basic 3-Way Handshake for Connection Synchronization


      TCP A                                                TCP B

  1.  ESTABLISHED                                          ESTABLISHED

  2.  (Close)
      FIN-WAIT-1  --> <SEQ=100><ACK=300><CTL=FIN,ACK>  --> CLOSE-WAIT

  3.  FIN-WAIT-2  <-- <SEQ=300><ACK=101><CTL=ACK>      <-- CLOSE-WAIT

  4.                                                       (Close)
      TIME-WAIT   <-- <SEQ=300><ACK=101><CTL=FIN,ACK>  <-- LAST-ACK

  5.  TIME-WAIT   --> <SEQ=101><ACK=301><CTL=ACK>      --> CLOSED

  6.  (2 MSL)
      CLOSED

                         Normal Close Sequence

                  

      Client                                               Server

      ClientHello                  -------->
                                                      ServerHello
                                                     Certificate*
                                               ServerKeyExchange*
                                              CertificateRequest*
                                   <--------      ServerHelloDone
      Certificate*
      ClientKeyExchange
      CertificateVerify*
      [ChangeCipherSpec]
      Finished                     -------->
                                               [ChangeCipherSpec]
                                   <--------             Finished
      Application Data             <------->     Application Data

             Figure 1.  Message flow for a full handshake(RFC 5246)
```

## 网络不可靠
### 物理层不可靠解决
以太网增加冗余链路，提高可靠。带来环路等问题
[轻松应用IEEE 802.1w协议](https://blog.csdn.net/CTO_51/article/details/8425881)

### 数据链路解决物理层不可靠
https://blog.csdn.net/CTO_51/article/details/8425881

以太网的MAC地址格式是12个16进制数，比如0800200A8C6D
因特网IP地址格式是4个点分10进制数，比如192.168.201.160
以太网的MAC地址，翻译成因特网的IP地址，这就是ARP的作用

数据链路层为了上层服务，链路上通过流量控制、分段/重组和差错控制来保证数据传输的可靠性，将物理层提供的可能出错的物理连接改造成为逻辑上无差错的数据链路
### 网络层不可靠
确定主机到主机，点对点
网络层的目的是实现两个端系统之间的数据透明传送，主要工作是路径选择、路由及逻辑寻址
### 传输层解决链路层的不可靠
确定端口到端口
传输层的一些协议是面向链接的，这就意味着传输层能保持对链路层分段的跟踪，并且重传那些失败的分段。


## TLS
### 基础
欧拉公式、费马小定理、中国剩余定理，可以说是三大加密算法的基础

三大公钥加密算法（RSA、离散对数、椭圆曲线）都依赖数论与群论的知识

常见 MD5,DES,RSA 算法
### SSL/TLS
[CA](https://en.wikipedia.org/wiki/Certificate_authority)
[TLS handshake](https://wiki.osdev.org/TLS_Handshake)
[TLS 协议标准](https://tools.ietf.org/html/rfc5246#section-8.1.2)
```
     TLS Handshake
                                              +--------------+-----------------------+
                                              |  CA          |      +-------------+  |
                                              |  Root_priKey |      | digit cert  |  |
     +-----------------------------------+    |  Root_pubKey |      |    pubKey   |  |      +----------------------+
     | +--------------------------------->    +--------------+      |    cert_info|  |      <--------------------+ |
     | |                                      |                     +-------------+  |                           | |
     | |                                      |                     | sign cert   |  |                           | |
     | |                                      |                     +-------------+  |                           | |
     | |                                      +--------------------------------------+                           | |
     | |                                                                                                         | |
     | |                                                         ^ provide pubKey                                | |
     | |                                                         | sign with                                     | |
     | |                 (RSA : old key exchange )               | ca root private key                           | |
     | |                                                         |                                               | |
     | |                 +-------------+                    +----+------+                +-------------+         | |    +-----+-----+
     | |                 | client(RSA) |                    |   server  |                |  client(DH) |         | |          |
     | |                 +-----+-------+                    +----+------+                +-----+-------+         | |          |
     | |                       |         ClientHello             |         ClientHello         |                 | |          |
     | |                       |  (Random Num/cipher suites)     |  (Random Num/cipher suites) |                 | |          |
     | |                       | +-------------------------->    | <----------------------+    |                 | |        1RTT
     | |                       |                                 |                             |                 | |          |
     | |                       |                                 |                             |                 | |          |
     | |                       |          ServerHello            |      ServerHello            |                 | |          |
     | |   verify cert         |   (Random Num/cipher suites)    | (Random Num/cipher suites)  |                 | |          |
     | |                       |        Certificate (pubKey)     |    Certificate (pubKey)     |                 | |          |
     | +------------------+    |                                 |  Key Exchange(p,g,pubKey)   |  ---------------+ |          |
     +-------------------->    | <---------------------------+   | +---------------------->    |  <----------------+    +-----+------+
            verify ok          |         ServerHelloDone         |     ServerHelloDone         |
                               |                                 |                             |
                               |       Client Key Exchange       |    Client Key Exchange      |
    premaster_secret=random()  |   ( chiper(premaster_secret) )  |      (client pubkey)        |  client_pubkey=random()
pubkey_chiper(premaster_secret)| +--------------------------->   | <----------------------+    |  premaster_secret=(p,g,pubKey,client_pubkey)
                               |       Change Cipher Spec        |     Change Cipher Spec      |                         +-----+------+
                               |      Encrypted Handshake        |   Encrypted Handshake       |                               |
                               |                                 |                             |                               +
                               |                                 |                             |                             1RTT
                               |       Change Cipher Spec        |     Change Cipher Spec      |                               |
                               | <--------------------------+    | +---------------------->    |                               |
                               |     Encrypted Handshake         |   Encrypted Handshake       |                         +-----+------+
                               |                                 |                             |
                                                                 +                             +

```
 [SSL/TLS 协议报文](https://www.cnblogs.com/findumars/p/5929775.html)
```
+---------------------------------------------------------------+
|                    APPLICATION PROTOCOL                       |
+-----------------+----------------------+----------------------+
|  SSL HandSHARK  |SSL Change Cipher Spec|   SSL Alert Protocol |
+-----------------+----------------------+----------------------+
|                      SSL RECORD（封装SSL/TLS握手协议或http数据）|
+---------------------------------------------------------------+
|                      TCP                                      |
+---------------------------------------------------------------+
|                      IP                                       |
+---------------------------------------------------------------+


+--------------------------------------+---------------+-------------+---------------+
|             |  0: SSL 3.0，     2: TLS 1.1,                                        |
| version     |  1: TLS 1.0，     3: TLS 2.1                                         |
+-------------+------------------------+-----------------+------------+--------------+
|             |                        |                 |            |              |
| Content Type| Handshake              | ChangeCipherSpec| Application| EncryptedAlert|
|             |                        |                 |            |              |
|             +----------------------------------------------------------------------+
|             | handshake type :       |                 |            |              |
|             | 0 HelloRequest         |                 |            |              |
|             | 1 ClientHello          |                 |            |              |
|             | 2 ServerHello          |                 |            |              |
|             | 11 Certificate         |                 |            |              |
|             | 12 ServerKeyExchange   |                 |            |              |
|             | 13 CertificateRequest  |                 |            |              |
|             | 14 ServerHelloDone     |                 |            |              |
|             | 15 CertificateVerify   |                 |            |              |
|             | 16 ClientKeyExchange   |                 |            |              |
|             | 20 Finished            |                 |            |              |
|             | 22 Encrypted Handshake |                 |            |              |
+-------------+----------------------------------------------------------------------+

```

**Cipher Suit**分为 四部分
```
密钥交换算法，用于决定客户端与服务器之间在握手的过程中如何认证，用到的算法包括RSA，Diffie-Hellman，ECDH，PSK等，非对称加密算法主要用于 交换对称加密算法的密钥，而非数据交换
加密算法，用于加密消息流，该名称后通常会带有两个数字，分别表示密钥的长度和初始向量的长度，比如DES 56/56, RC2 56/128, RC4 128/128, AES 128/128, AES 256/256
报文认证信息码（MAC）算法，用于创建报文摘要，确保消息的完整性（没有被篡改），算法包括MD5，SHA，SHA256等。
PRF（伪随机数函数），用于生成“master secret”。

```

例如：TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA

```
基于TLS协议的；
使用ECDHE、RSA作为密钥交换算法；
加密算法是AES（密钥和初始向量的长度都是256）；
MAC算法（这里就是哈希算法）是SHA。
``` 
[密钥协商类型一，RSA](https://blog.csdn.net/andylau00j/article/details/54583769)
[RSA 证明过程](https://www.di-mgt.com.au/rsa_theory.html)
[RSA key生成过程 RSAKeyPairGenerator](openjdk/jdk/src/share/classes/sun/security/rsa/RSAKeyPairGenerator.java)介绍 n,p,q（p > q）,φ(n),e（RSAKeyGenParameterSpec.F4=65537）,d的胜场

```
public KeyPair generateKeyPair() {
        // accommodate odd key sizes in case anybody wants to use them
        int lp = (keySize + 1) >> 1;
        int lq = keySize - lp;
        if (random == null) {
            random = JCAUtil.getSecureRandom();
        }
        BigInteger e = publicExponent;
        while (true) {
            // generate two random primes of size lp/lq
            BigInteger p = BigInteger.probablePrime(lp, random);
            BigInteger q, n;
            do {
                q = BigInteger.probablePrime(lq, random);
                // convention is for p > q
                if (p.compareTo(q) < 0) {
                    BigInteger tmp = p;
                    p = q;
                    q = tmp;
                }
                // modulus n = p * q
                n = p.multiply(q);
                // even with correctly sized p and q, there is a chance that
                // n will be one bit short. re-generate the smaller prime if so
            } while (n.bitLength() < keySize);

            // phi = (p - 1) * (q - 1) must be relative prime to e
            // otherwise RSA just won't work ;-)
            BigInteger p1 = p.subtract(BigInteger.ONE);
            BigInteger q1 = q.subtract(BigInteger.ONE);
            BigInteger phi = p1.multiply(q1);
            // generate new p and q until they work. typically
            // the first try will succeed when using F4
            if (e.gcd(phi).equals(BigInteger.ONE) == false) {//不是互质，重新找p,q
                continue;
            }

            // private exponent d is the inverse of e mod phi
            BigInteger d = e.modInverse(phi);  //e*d 与phi欧拉函数互质

            // 1st prime exponent pe = d mod (p - 1)
            BigInteger pe = d.mod(p1);
            // 2nd prime exponent qe = d mod (q - 1)
            BigInteger qe = d.mod(q1);

            // crt coefficient coeff is the inverse of q mod p
            BigInteger coeff = q.modInverse(p);

            try {
                PublicKey publicKey = new RSAPublicKeyImpl(n, e);
                PrivateKey privateKey =
                        new RSAPrivateCrtKeyImpl(n, e, d, p, q, pe, qe, coeff);
                return new KeyPair(publicKey, privateKey);
            } catch (InvalidKeyException exc) {
                // invalid key exception only thrown for keys < 512 bit,
                // will not happen here
                throw new RuntimeException(exc);
            }
        }
    }

```

```
1. 客户端连上服务端
2. 服务端发送 CA 证书给客户端
3. 客户端验证该证书的可靠性
4. 客户端从 CA 证书中取出公钥
5. 客户端生成一个随机密钥 k，并用这个公钥加密得到 k'
6. 客户端把 k' 发送给服务端
7. 服务端收到 k' 后用自己的私钥解密得到 k
8. 此时双方都得到了密钥 k，协商完成。
```

密钥协商类型二，hm（离散对数问题）
```
1. 客户端先连上服务端
2. 服务端生成一个随机数 y1 作为自己的私钥，然后根据算法参数计算出公钥 b1（算法参数通常是固定的）
3. 服务端使用某种签名算法把“算法参数（模数p，基数g）和服务端公钥b1”作为一个整体进行签名
4. 服务端把“算法参数（模数p，基数a）、服务端公钥b1、签名”发送给客户端
5. 客户端收到后验证签名是否有效
6. 客户端生成一个随机数 y2 作为自己的私钥，然后根据算法参数计算出公钥 b2
7. 客户端把 b2 发送给服务端
8. 客户端和服务端（根据上述 DH 算法）各自计算出 k 作为会话密钥


定义（数论中同余，指数，原根等概念）：
a^y1≡b1 (mod p)
a^y2≡b2 (mod p)

计算：
K=(b2)^y1 mod p
或
K=(b1)^y2 mod p
```
密钥协商类型三，ECDH（ 依赖的是——求解“椭圆曲线离散对数问题”的困难。）
密钥协商类型四，PSK 
```
　　在通讯【之前】，通讯双方已经预先部署了若干个共享的密钥。
　　为了标识多个密钥，给每一个密钥定义一个唯一的 ID
　　协商的过程很简单：客户端把自己选好的密钥的 ID 告诉服务端。
　　如果服务端在自己的密钥池子中找到这个 ID，就用对应的密钥与客户端通讯；否则就报错并中断连接。
```
密钥协商类型五，SRP 
```
 client/server 双方共享的是比较人性化的密码（password）而不是密钥（key）。该算法采用了一些机制（盐/salt、随机数）来防范“嗅探/sniffer”或“字典猜解攻击”或“重放攻击”。
```


## 常见网络协议

```
                  +-----------------+
                  |    HTTP/2       |
                  +-----------------+
                  |     WebSocket   |
                  |      +----------+
                  |      |  HTTPs   |
+-----------+     +-----------------+
| Http/3    |     | Http | TLS/SSL  |
|  QUIC     +-----+------+----------+
|           |    Socket             |
+-----------------------------------+
|  UDP      |      TCP              |
+-----------+-----------------------+
|          DNS(1RTT)                |
+-----------+-----------------------+



+------------+--------+-----------+------------+----------+
|            |        |           |            |          |
|            |  tcp   |   Https   |  websocket |    (QUIC)|
+---------------------------------------------------------+
|            |        |           |            |          |
|handshake   |  3     |           |            |   0/1    |
+---------------------------------------------------------+
|            |        |           |            |          |
|performation|        |           |            |          |
| (RTT)      |  1     |  7(worst) |2(tcp+http) |          |          
|            |        |           |            |          |
+------------+--------+-----------+------------+----------+
https://www.cnblogs.com/mylanguage/p/5635524.html
https://liudanking.com/network/tls1-3-quic-是怎样做到-0-rtt-的/

```
HTTP（HTTPS）所有数据都作为文本处理，广泛应用于网络访问，是公有协议，有专门机构维护。
RTSP流媒体协议，多用在监控领域视频直播点播；是公有协议，有专门机构维护。
RTMP流媒体协议，多用在互联网直播领域（RTMP+CDN）、视频会议（取代原来SIP），是Adobe的私有协议,未完全公开。
RTSP传输一般需要2-3个通道，命令和数据通道分离，HTTP和RTMP一般在TCP一个通道上传输命令和数据。RTMP协议一般传输的是flv，f4v格式流，RTSP协议一般传输的是ts,mp4格式的流。HTTP没有特定的流。

## 算法（编码，数字摘要，加密/密钥协商/密钥交换）
``` 
+--------------------------------------------------------------------------+
|                                                                          |
|                    +--------------------+--------------------------------+
|                    | MIME      URL      |  Base64   url/email            |
+--------------------+--------------------+--------------------------------+
|       (hash)                                                             |
|                 MD5 SHA-1 SHA256                                         |
+--------------------------------------------------------------------------+
|       RSA DH                                                             |
+--------------------------------------------------------------------------+
|       AES                                                                |
+--------------------------------------------------------------------------+
|       TCP/IP                                                             |
+--------------------------------------------------------------------------+

```
## TCP/IP

```
+---------------------------------------------------------------------------+
|  TCP/IP Protocol Architecture Model                                       |
+---------------------+----------------------+------------------------------+
|  OSI Ref. Layer No. | OSI Layer Equivalent |  TCP/IP Protocol Examples    |
+---------------------------------------------------------------------------+
|                     |     Application,     | NFS, NIS+, DNS, telnet ,     |
|  5,6,7              |     session,         |  ftp , rlogin , rsh , rcp ,  |
|                     |     presentation     | RIP, RDISC, SNMP, and others |RPC
+---------------------------------------------------------------------------+
|  4                  |     Transport        |     TCP, UDP                 |TCP还支持重传，错误校验，保证数据的可靠性,UDP不支持重传，有校验
+---------------------------------------------------------------------------+
|  3                  |     Network          |     IP, ARP, ICMP            |拥塞控制和路由选择
+---------------------------------------------------------------------------+
|  2                  |     Data link        |    PPP, IEEE 802.2           |
+---------------------+----------------------+------------------------------+

```
 
TCP：面向连接、传输可靠(保证数据正确性,保证数据顺序)、用于传输大量数据(流模式)、速度慢，建立连接需要开销较多(时间，系统资源)，一般用于文件传输、邮件需要精确的操作。
UDP：面向非连接、传输不可靠、用于传输少量数据(数据包模式)、速度快，一般用于即时通信、在线视频需要低延迟的操作。
```

```

TCP如何保证可靠的？
通过顺序编号和确认（ACK）来实现的

1. 流量控制是管理两端的流量，以免会产生发送过块导致收端溢出，或者因收端处理太快而浪费时间的状态。
   流量控制就是让发送方的发送速率不要太快，要让接收方来得及接收。
   用的是：滑动窗口，以字节为单位

2. 拥塞控制：如果网络上的负载（发送到网络上的分组数）大于网络上的容量（网络同时能处理的分组数），就可能引起拥塞。
判断网络拥塞的两个因素：延时和吞吐量。
拥塞控制机制是：开环（预防）和闭环（消除）
tcp处理拥塞的三种策略：慢启动（指数增大），拥塞避免（加法增大），拥塞检测（除2减少，或叫做乘法减少），快速重传，快速恢复 

3. 差错控制：TCP必须保证数据：按序，没有差错，没有部分丢失，没有重复的交给应用层。方法就是：校验和，确认，超时重传


## RPC


RPC/REST/GraphQL
	           耦合性   约束性	 复杂度	 缓存	 可发现性	版本控制
RPC(Function)	high	medium	low	   custom	bad	    hard
REST(Resource)	low	    low	    low	    http	good	easy
GraphQL(Query)	medium	high	medium	custom	good	???


mqtt/xmqq/CoAP 协议
XMPP的优点：协议成熟、强大、可扩展性强、目前主要应用于许多聊天系统中。缺点：协议较复杂、冗余（基于XML）、费流量、费电，部署硬件成本高。
MQTT的优点：协议简洁、小巧、可扩展性强、省流量、省电。缺点：不够成熟、实现较复杂、服务端组件rsmb不开源，部署硬件成本较高。

## mqtt 
mqtt提供了qos0、qos1和qos2的不同的消息发送的服务质量，增强可靠性
[Protocol Specifications](http://mqtt.org/documentation)
```
+-----------------------------------------------------------------+                     +-----------------------------------+
|                                                                 |                     |     Broker                        |
|                                         +-------------------+   |                     |                                   |
|                                         |  Publish          |   |      Topic          |                                   |
|                                         |                   |  +-------------------------->                               |
|                                    +->  |                   |   |                     |                                   |
|                                    |    +-------------------+   |                     |                                   |
| +--------------------------------+ |                            |                     |                                   |
| |             Fixed header       |+|                            |                     |                                   |
| +--------------------------------+      +-------------------+   |                     |                                   |
| |                                |      |  Subscribe        |  +--------------------------->                              |
| |             Variable header    |      |                   |   |                     |                                   |
| +--------------------------------+  +-> |                   |   |                     |                                   |
| |                                |      |                   |  <---------------------------+                              |
| |             payload            |      |                   |   |                     |                                   |
| +--------------------------------+      +-------------------+   |                     |                                   |
+-----------------------------------------------------------------+                     +-----------------------------------+

```


```Fixed header

+---+---+---+---+---+---+---+---+
|   |   |   |   |   |   |   |   |
| 7 | 6 | 5 | 4 | 3 | 2 | 1 | 0 |bit
|   |   |   |   |   |   |   |   |
+---+---+---+-----------+-------+
|               | D |       |   |
| Message Type  | U | Qos   | R |
|               | P |       |   |
+---------------+---+-------+---+
|                               |
|       Remaining Length        |
|                               |
+-------------------------------+

```
### 消息体
``` [智物客](https://blog.csdn.net/illusion116/article/details/76623014 )
（1）CONNECT，消息体内容主要是：客户端的ClientID、订阅的Topic、Message以及用户名和密码。
（2）SUBSCRIBE，消息体内容是一系列的要订阅的主题以及QoS。
（3）SUBACK，消息体内容是服务器对于SUBSCRIBE所申请的主题及QoS进行确认和回复。
（4）UNSUBSCRIBE，消息体内容是要订阅的主题。
``` 
  
### eclipse mqtt lib / ibm broker


broker
- [Eclipse Mosquitto](https://mosquitto.org/)
- [moquette](https://github.com/andsel/moquette)


## xmpp
```
+----------------------------------+                  +----------------------------------+
|  asmack                          |  +------------>  |  openfire                        |
|                                  |                  |                                  |
+----------------------------------+  <------------+  |                                  |
|   XMPP                           |                  |                                  |
|                                  |                  |                                  |
|   ^message/^,^presence/^,^iq/^   |                  |                                  |
|                                  |                  |                                  |
+----------------------------------+                  |                                  |
|                                  |                  |                                  |
|     SASL                         |                  |                                  |
+----------------------------------+                  |                                  |
|                                  |                  |                                  |
|     TSL                          |                  |                                  |
+----------------------------------+                  |                                  |
|     TCP                          |                  |                                  |
|                                  |                  |                                  |
+----------------------------------+                  +----------------------------------+


```
    1.其中TCP保证了这是一个可靠的链路。
    2.TSL主要采用STARTTLS加密算法保证传输数据的安全。
    3.SASL用来认证客户端的真实有效性。
### 协议
Jabber协议
XMPP通信原语有3种：message、presence和iq。


### 服务器实现 openfire，spark


### 客户端实现 smack asmack


### HTTP UA
[UA 介绍](https://www-archive.mozilla.org/build/revised-user-agent-strings.html)
http://useragentstring.com/
https://developer.chrome.com/multidevice/user-agent


## Netty

```
* 001_initial aef2ab453a8b11984c4fe64ba27612a1308ee490 Initial import.  Needs to: * rename packages * update license information

+------------------------------------------------------------------------------------------------------------+
|  ServerBootstrap:Bootstrap                                                                                 |
|         factory:NioServerSocketChannelFactory                                                              |
|         bind()                                                                                             |
+------------------------------------------------------------------------------------------------------------+
|  NioServerSocketChannelFactory                     DefaultChannelPipeline                                  |
|       bossExecutor:Executor                           name2ctx                          head,tail          |
|       sink:NioServerSocketPipelineSink                channel:NioServerSocketChannel                       |
|       newChannel():NioClientSocketChannel             sink:NioServerSocketPipelineSink  sendUpstream()     |
|                                                       attach()                          sendDownstream()   |
+------------------------------------------------------------------------------------------------------------+
|  Boss:Runnable  NioServerSocketPipelineSink       NioServerSocketChannel:AbstractChannel                   |
|                      workers:NioWorker[]               socket:ServerSocketChannel                          |
|                      bind()                            pipeline:ChannelPipeline                            |
+------------------------------------------------------------------------------------------------------------+
|   NioWorker:Runnable                                          DefaultChannelHandlerContext                 |
|      executor:Executor                                              :ChannelHandlerContext                 |
|      selector:Selector                                            handler:ChannelHandler                   |
|      processSelectedKeys()                                                                                 |
|      fireMessageReceived()                                    SimpleChannelHandler                         |
|      register(channel,future)                                      :ChannelUpstreamHandler                 |
+------------------------------------------------------------------------------------------------------------+
|      Channels                                                 Binder:SimpleChannelHandler                  |
|         pipeline():DefaultChannelPipeline                        channelOpen()                             |
|         fireMessageReceived()                                                                              |
|                                                                                                            |
+------------------------------------------------------------------------------------------------------------+



```