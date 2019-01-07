mqtt/xmqq/CoAP 协议

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