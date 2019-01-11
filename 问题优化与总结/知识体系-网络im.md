mqtt/xmqq/CoAP 协议
XMPP的优点：协议成熟、强大、可扩展性强、目前主要应用于许多聊天系统中。缺点：协议较复杂、冗余（基于XML）、费流量、费电，部署硬件成本高。
MQTT的优点：协议简洁、小巧、可扩展性强、省流量、省电。缺点：不够成熟、实现较复杂、服务端组件rsmb不开源，部署硬件成本较高。

## mqtt 
[Protocol Specifications](http://mqtt.org/documentation)


```

+---+---+---+---+---+---+---+---+
|   |   |   |   |   |   |   |   |
| 7 | 6 | 5 | 4 | 3 | 2 | 1 | 0 |
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


### eclipse mqtt lib / ibm broker


broker
- [Eclipse Mosquitto](https://mosquitto.org/)
- [moquette](https://github.com/andsel/moquette)


## xmpp
+----------------+
|                |
|     XMPP       |
|                |
+----------------+
|                |
|     SASL       |
|                |
+----------------+
|                |
|     TSL        |
|                |
+----------------+
|                |
|     TCP        |
|                |
+----------------+
    1.其中TCP保证了这是一个可靠的链路。
    2.TSL主要采用STARTTLS加密算法保证传输数据的安全。
    3.SASL用来认证客户端的真实有效性。
### 协议
XMPP通信原语有3种：message、presence和iq。


### 服务器实现 openfire，spark


###