#Android Socket ServerSocket
> 

@(源码分析)[Socket|Android]

 

- **创建及初始化**
- **connection** 

---------------------

[TOC]

##创建及初始化
构造方法（过期的构造方法就不列出了）
```java
    public Socket() {
        this.impl = factory != null ? factory.createSocketImpl() : new PlainSocketImpl();
        this.proxy = null;
    }
    public Socket(Proxy proxy) {
        if (proxy == null || proxy.type() == Proxy.Type.HTTP) {
            throw new IllegalArgumentException("Invalid proxy: " + proxy);
        }
        this.proxy = proxy;
        this.impl = factory != null ? factory.createSocketImpl() : new PlainSocketImpl(proxy);
    }
    //--------step 1
    public Socket(String dstName, int dstPort) throws UnknownHostException, IOException {
        this(dstName, dstPort, null, 0);
    }
    //--------step 2
    public Socket(String dstName, int dstPort, InetAddress localAddress, int localPort) throws IOException {
        this();
        tryAllAddresses(dstName, dstPort, localAddress, localPort, true);
    }
    public Socket(InetAddress dstAddress, int dstPort) throws IOException {
        this();
        checkDestination(dstAddress, dstPort);
        startupSocket(dstAddress, dstPort, null, 0, true);
    }
    public Socket(InetAddress dstAddress, int dstPort,
            InetAddress localAddress, int localPort) throws IOException {
        this();
        checkDestination(dstAddress, dstPort);
        startupSocket(dstAddress, dstPort, localAddress, localPort, true);
    }
    protected Socket(SocketImpl impl) throws SocketException {
        this.impl = impl;
        this.proxy = null;
    }
```
然而，本文只分析Socket(String dstName, int dstPort)这个常用的构造方法。
1. 初始化impl属性，默认是PlainSocketImpl对象，proxy设置为null

2. tryAllAddresses Tries to connect a socket to all IP addresses of the given hostname.
```java
    private void tryAllAddresses(String dstName, int dstPort, InetAddress
            localAddress, int localPort, boolean streaming) throws IOException {
        InetAddress[] dstAddresses = InetAddress.getAllByName(dstName);
        // Loop through all the destination addresses except the last, trying to
        // connect to each one and ignoring errors. There must be at least one
        // address, or getAllByName would have thrown UnknownHostException.
        InetAddress dstAddress;
        for (int i = 0; i < dstAddresses.length - 1; i++) {
            dstAddress = dstAddresses[i];
            try {
                checkDestination(dstAddress, dstPort);
                startupSocket(dstAddress, dstPort, localAddress, localPort, streaming);
                return;
            } catch (IOException ex) {
            }
        }

        // Now try to connect to the last address in the array, handing back to
        // the caller any exceptions that are thrown.
        dstAddress = dstAddresses[dstAddresses.length - 1];
        checkDestination(dstAddress, dstPort);
        startupSocket(dstAddress, dstPort, localAddress, localPort, streaming);
    }
```
InetAddress.getAllByName(dstName);获取目标的所有地址
```java
    private static InetAddress[] getAllByNameImpl(String host, int netId) throws UnknownHostException {
        if (host == null || host.isEmpty()) {
            return loopbackAddresses();
        }

        // Is it a numeric address?
        InetAddress result = parseNumericAddressNoThrow(host);
        if (result != null) {
            result = disallowDeprecatedFormats(host, result);
            if (result == null) {
                throw new UnknownHostException("Deprecated IPv4 address format: " + host);
            }
            return new InetAddress[] { result };
        }

        return lookupHostByName(host, netId).clone();
    }
```
-- 如果ip地址为null,返回本地环回接口（或地址），亦称回送地址()。the IPv6 loopback address {@code ::1} or the IPv4 loopback address {@code 127.0.0.1}.IPv6是冒号间隔的，IPv4是点好隔开的。
-- 通过parseNumericAddressNoThrow方法解析ip地址，调用Libcore.os.android_getaddrinfo获取到InetAddress数组,取第一条信息。
-- disallowDeprecatedFormats 解析
--- Libcore.os.inet_pton：将“点分十进制” －> “二进制整数”
--- inet_ntop：将“冒号分十进制” －> “二进制整数”
---- AF_INET（又称 PF_INET）是 IPv4 网络协议的套接字类型，
---- AF_INET6 则是 IPv6 的；
---- 而 AF_UNIX 则是 Unix 系统本地通信。

-- 解析非IP的host
--- java.net.InetAddress#addressCache获取缓存数据，如果有的话
--- Libcore.os.android_getaddrinfo 获取并，缓存在addressCache
--- 如果出现异常则放在异常容器中 addressCache.putUnknownHost(host, netId, detailMessage);

到现在已经分析完了
```java
    InetAddress[] dstAddresses = InetAddress.getAllByName(dstName);
```
接下来遍历获取的 *InetAddress[] dstAddresses*进行socket连接
1. checkDestination验证端口大于 *0*，小于 *65535*
2. startupSocket通过impl属性，进行连接

impl连接的过程
1. impl.create(streaming); streaming 为ture 需要返回streaming socket；否则datagram socket
2. impl.bind(addr, localPort);
3. cacheLocalAddress();
4. impl.connect(dstAddress, dstPort);
5. cacheLocalAddress();

impl默认是 *java.net.PlainSocketImpl.java* ，create方法如下
```java
    @Override
    protected void create(boolean streaming) throws IOException {
        this.streaming = streaming;
        this.fd = IoBridge.socket(streaming);
    }
    public final class Libcore {
        private Libcore() { }

        public static Os os = new BlockGuardOs(new Posix());
    }
```
-- 通过IoBridge获取到的文件描述。 
-- 执行Posix的本地方法
```java
    public native FileDescriptor socket(int domain, int type, int protocol) throws ErrnoException;
```
本地文件位于libcore/luni/src/main/native/libcore_io_Posix.cpp
```C
    static jobject Posix_socket(JNIEnv* env, jobject, jint domain, jint type, jint protocol) {
        int fd = throwIfMinusOne(env, "socket", TEMP_FAILURE_RETRY(socket(domain, type, protocol)));
        return fd != -1 ? jniCreateFileDescriptor(env, fd) : NULL;
    }
```
bionic/libc/bionic/socket.cpp
```C
    int socket(int domain, int type, int protocol) {
        return __netdClientDispatch.socket(domain, type, protocol);
    }
```
稍微提下Bionic
>>Bionic是Android的C/C++ library, libc是GNU/Linux以及其他类Unix系统的基础函数库，
最常用的就是GNU的libc，也叫glibc。Android之所以采用bionic而不是glibc，有几个原因：
1、版权问题，因为glibc是LGPL
2、库的体积和速度，bionic要比glibc小很多。
3、提供了一些Android特定的函数，getprop LOGI等。
>>socket是网络编程的基础，Android中socket接口定义在bionic里面，Android5.0后，为了更好使用fwmark，用netd中的函数实现部分代替了bionic的socket实现。[ Android5.0网络之socket接口的改变](http://blog.csdn.net/javon_hzw/article/details/46622723)

根据上面的文章，我们知道__netdClientDispatch创建的位置在bionic/libc/bionic/NetdClient.cpp，先看如何调用NetdClient
```CPP
    // We flag the __libc_preinit function as a constructor to ensure
    // that its address is listed in libc.so's .init_array section.
    // This ensures that the function is called by the dynamic linker
    // as soon as the shared library is loaded.
    __attribute__((constructor)) static void __libc_preinit() {
      // Read the kernel argument block pointer from TLS.
      void** tls = __get_tls();
      KernelArgumentBlock** args_slot = &reinterpret_cast<KernelArgumentBlock**>(tls)[TLS_SLOT_BIONIC_PREINIT];
      KernelArgumentBlock* args = *args_slot;

      // Clear the slot so no other initializer sees its value.
      // __libc_init_common() will change the TLS area so the old one won't be accessible anyway.
      *args_slot = NULL;

      __libc_init_common(*args);

      // Hooks for various libraries to let them know that we're starting up.
      malloc_debug_init();
      netdClientInit();
    }
```
 上面的代码调用了netdClientInit()，实际pthread执行NetdClient.cpp的netdClientInitImpl
```CPP
    template <typename FunctionType>
    static void netdClientInitFunction(void* handle, const char* symbol, FunctionType* function) {
        typedef void (*InitFunctionType)(FunctionType*);
        InitFunctionType initFunction = reinterpret_cast<InitFunctionType>(dlsym(handle, symbol));
        if (initFunction != NULL) {
            initFunction(function);
        }
    }

    static void netdClientInitImpl() {
        void* netdClientHandle = dlopen("libnetd_client.so", RTLD_LAZY);
        if (netdClientHandle == NULL) {
            // If the library is not available, it's not an error. We'll just use
            // default implementations of functions that it would've overridden.
            return;
        }
        netdClientInitFunction(netdClientHandle, "netdClientInitAccept4",
                               &__netdClientDispatch.accept4);
        netdClientInitFunction(netdClientHandle, "netdClientInitConnect",
                               &__netdClientDispatch.connect);
        netdClientInitFunction(netdClientHandle, "netdClientInitNetIdForResolv",
                               &__netdClientDispatch.netIdForResolv);
        netdClientInitFunction(netdClientHandle, "netdClientInitSocket", &__netdClientDispatch.socket);
    }

    static pthread_once_t netdClientInitOnce = PTHREAD_ONCE_INIT;

    extern "C" __LIBC_HIDDEN__ void netdClientInit() {
        if (pthread_once(&netdClientInitOnce, netdClientInitImpl)) {
            __libc_format_log(ANDROID_LOG_ERROR, "netdClient", "Failed to initialize netd_client");
        }
    }
```
system/netd/client/NetdClient.cpp的netdClientInitSocket具体是bionic/libc/bionic/NetdClient.cpp中netdClientInitFunction中的参数,最终通过FwmarkServer对socket做mark,如下面的代码
```CPP
    int netdClientConnect(int sockfd, const sockaddr* addr, socklen_t addrlen) {
        if (sockfd >= 0 && addr && FwmarkClient::shouldSetFwmark(addr->sa_family)) {
            FwmarkCommand command = {FwmarkCommand::ON_CONNECT, 0, 0};
            if (int error = FwmarkClient().send(&command, sizeof(command), sockfd)) {
                errno = -error;
                return -1;
            }
        }
        return libcConnect(sockfd, addr, addrlen);
    }
```
连接交由FwmarkClient（system/netd/client/FwmarkClient.cpp）
```CPP
int FwmarkClient::send(void* data, size_t len, int fd) {
    mChannel = socket(AF_UNIX, SOCK_STREAM, 0);
    if (mChannel == -1) {
        return -errno;
    }

    if (TEMP_FAILURE_RETRY(connect(mChannel, reinterpret_cast<const sockaddr*>(&FWMARK_SERVER_PATH),
                                   sizeof(FWMARK_SERVER_PATH))) == -1) {
        // If we are unable to connect to the fwmark server, assume there's no error. This protects
        // against future changes if the fwmark server goes away.
        return 0;
    }

    iovec iov;
    iov.iov_base = data;
    iov.iov_len = len;

    msghdr message;
    memset(&message, 0, sizeof(message));
    message.msg_iov = &iov;
    message.msg_iovlen = 1;

    union {
        cmsghdr cmh;
        char cmsg[CMSG_SPACE(sizeof(fd))];
    } cmsgu;

    memset(cmsgu.cmsg, 0, sizeof(cmsgu.cmsg));
    message.msg_control = cmsgu.cmsg;
    message.msg_controllen = sizeof(cmsgu.cmsg);

    cmsghdr* const cmsgh = CMSG_FIRSTHDR(&message);
    cmsgh->cmsg_len = CMSG_LEN(sizeof(fd));
    cmsgh->cmsg_level = SOL_SOCKET;
    cmsgh->cmsg_type = SCM_RIGHTS;
    memcpy(CMSG_DATA(cmsgh), &fd, sizeof(fd));

    if (TEMP_FAILURE_RETRY(sendmsg(mChannel, &message, 0)) == -1) {
        return -errno;
    }

    int error = 0;

    if (TEMP_FAILURE_RETRY(recv(mChannel, &error, sizeof(error), 0)) == -1) {
        return -errno;
    }

    return error;
}

```
<br/>
cacheLocalAddress将create的获取的文件描述，赋值给localAddress
```java
    private void cacheLocalAddress() throws SocketException {
        this.localAddress = IoBridge.getSocketLocalAddress(impl.fd);
    }
```
bind方法还是用IoBridge进行处理
```java
    @Override protected void bind(InetAddress address, int port) throws IOException {
        IoBridge.bind(fd, address, port);
        if (port != 0) {
            this.localport = port;
        } else {
            this.localport = IoBridge.getSocketLocalPort(fd);
        }
    }
```
connect
```java
    protected void connect(InetAddress anAddr, int aPort) throws IOException {
        connect(anAddr, aPort, 0);
    }
    private void connect(InetAddress anAddr, int aPort, int timeout) throws IOException {
        InetAddress normalAddr = anAddr.isAnyLocalAddress() ? InetAddress.getLocalHost() : anAddr;
        if (streaming && usingSocks()) {//使用代理
            socksConnect(anAddr, aPort, 0);
        } else {
            IoBridge.connect(fd, normalAddr, aPort, timeout);
        }
        address = normalAddr;
        port = aPort;
    }
    private void socksConnect(InetAddress applicationServerAddress, int applicationServerPort, int timeout) throws IOException {
        try {
            IoBridge.connect(fd, socksGetServerAddress(), socksGetServerPort(), timeout);
        } catch (Exception e) {
            throw new SocketException("SOCKS connection failed", e);
        }

        socksRequestConnection(applicationServerAddress, applicationServerPort);

        lastConnectedAddress = applicationServerAddress;
        lastConnectedPort = applicationServerPort;
    }
```
如果用代理连接，调用socksConnect方法，否则直接用IoBridge连接<br/>

sucks（代理）发送java.net.Socks4Message（Socks4Message.COMMAND_CONNECT），接收java.net.Socks4Message（Socks4Message.REPLY_LENGTH 返回8个byte）
```java
    /**
     * Send a SOCKS V4 request.
     */
    private void socksSendRequest(int command, InetAddress address, int port) throws IOException {
        Socks4Message request = new Socks4Message();
        request.setCommandOrResult(command);
        request.setPort(port);
        request.setIP(address.getAddress());
        request.setUserId("default");

        getOutputStream().write(request.getBytes(), 0, request.getLength());
    }

    /**
     * Read a SOCKS V4 reply.
     */
    private Socks4Message socksReadReply() throws IOException {
        Socks4Message reply = new Socks4Message();
        int bytesRead = 0;
        while (bytesRead < Socks4Message.REPLY_LENGTH) {
            int count = getInputStream().read(reply.getBytes(), bytesRead,
                    Socks4Message.REPLY_LENGTH - bytesRead);
            if (count == -1) {
                break;
            }
            bytesRead += count;
        }
        if (Socks4Message.REPLY_LENGTH != bytesRead) {
            throw new SocketException("Malformed reply from SOCKS server");
        }
        return reply;
    }
```

正常练级
##connection
