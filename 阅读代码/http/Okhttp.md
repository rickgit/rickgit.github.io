#OkHttp3.2

>
复用连接池 
缓存策略
DiskLruCache  LinkedHashMap
任务队列 线程池文,反向代理模型,OkHttp的任务调度

@(源码分析)[OkHttp3.2|Android]

 

- **创建及初始化**
- **connection** 

---------------------

[TOC]

##创建及初始化
###HttpClient
```java
    OkHttpClient okHttpClient = new OkHttpClient();

    public OkHttpClient() {
      this(new Builder());
    }
    private OkHttpClient(Builder builder) {
        ···
        ···
        ···
    }
```
提供一个public的构造方法。
-- 创建Builder，并把默认构建的信息，参数方式传给另一个构造方法，进行赋值
-- private的构造方法，

先看下Builder的创建，这个类用来构造URL的信息，采用构建者模式。
```java
    public Builder() {
      dispatcher = new Dispatcher();//异步请求调度
      protocols = DEFAULT_PROTOCOLS;//Http协议 包括http2,http1_1,spdy
      connectionSpecs = DEFAULT_CONNECTION_SPECS;//数组 包括元素 ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT
      proxySelector = ProxySelector.getDefault();//代理选择，默认是java.net.ProxySelectorImpl
      cookieJar = CookieJar.NO_COOKIES;//使用cookie
      socketFactory = SocketFactory.getDefault();//socket工厂，默认javax.net.DefaultSocketFactory
      hostnameVerifier = OkHostnameVerifier.INSTANCE;
      certificatePinner = CertificatePinner.DEFAULT;
      proxyAuthenticator = Authenticator.NONE;
      authenticator = Authenticator.NONE;
      connectionPool = new ConnectionPool();//连接池
      dns = Dns.SYSTEM; //
      followSslRedirects = true;// ssl重定向
      followRedirects = true;//重定向
      retryOnConnectionFailure = true;// 重连
      connectTimeout = 10_000; // 连接超时时间
      readTimeout = 10_000;//读取时间
      writeTimeout = 10_000;//写超时时间
    }
```
默认初始化了一些信息。

- ConnectionSpec 连接的相关规范 tls的密钥,交换密钥,hash算法.默认有三种连接规范,cipherSuites为tls加密的加密套件,百度首页用的是 **CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256**
okhttp3.CipherSuite包含了所有加密套件,但只有少量在Android平台支持,定义在okhttp3.ConnectionSpec.APPROVED_CIPHER_SUITES.
```java

  /** A modern TLS connection with extensions like SNI and ALPN available. */
  public static final ConnectionSpec MODERN_TLS = new Builder(true)
      .cipherSuites(APPROVED_CIPHER_SUITES)
      .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
      .supportsTlsExtensions(true)
      .build();

  /** A backwards-compatible fallback connection for interop with obsolete servers. */
  public static final ConnectionSpec COMPATIBLE_TLS = new Builder(MODERN_TLS)
      .tlsVersions(TlsVersion.TLS_1_0)
      .supportsTlsExtensions(true)
      .build();

  /** Unencrypted, unauthenticated connections for {@code http:} URLs. */
  public static final ConnectionSpec CLEARTEXT = new Builder(false).build();

```
值得注意ConnectionSpec的hashCode方法,哈希算法参考 [如何生成一个合适的hashcode方法](http://www.importnew.com/8189.html)

- ProxySelectorImpl 用来创建Proxy代理对象.能用代理的有http代理,socks代理
1. HTTP代理：能够代理客户机的HTTP访问，主要是代理浏览器访问网页，它的端口一般为80、8080、3128等；
2. SOCKS代理：SOCKS代理与其他类型的代理不同，它只是简单地传递数据包，而并不关心是何种应用协议，既可以是HTTP请求，所以SOCKS代理服务器比其他类型的代理服务器速度要快得多。
SOCKS代理又分为SOCKS4和SOCKS5，二者不同的是SOCKS4代理只支持TCP协议（即传输控制协议），而SOCKS5代理则既支持TCP协议又支持UDP协议（即用户数据包协议），还支持各种身份验证机制、服务器端域名解析等。
SOCK4能做到的SOCKS5都可得到，但SOCKS5能够做到的SOCK4则不一定能做到，比如我们常用的聊天工具QQ在使用代理时就要求用SOCKS5代理，因为它需要使用UDP协议来传输数据。

```java
    public enum Type {
        /**
         * Direct connection. Connect without any proxy.
         */
        DIRECT,

        /**
         * HTTP type proxy. It's often used by protocol handlers such as HTTP,
         * HTTPS and FTP.
         */
        HTTP,

        /**
         * SOCKS type proxy.
         */
        SOCKS
    }
```

- CookieJar接口, cookie 策略和持久化方法,含有两个方法,提供没有cookie的实现. **A reasonable policy is to reject all cookies**

```java
public interface CookieJar {
  /** A cookie jar that never accepts any cookies. */
  CookieJar NO_COOKIES = new CookieJar() {
    @Override public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
    }

    @Override public List<Cookie> loadForRequest(HttpUrl url) {
      return Collections.emptyList();
    }
  };

  /**
   * Saves {@code cookies} from an HTTP response to this store according to this jar's policy.
   *
   * <p>Note that this method may be called a second time for a single HTTP response if the response
   * includes a trailer. For this obscure HTTP feature, {@code cookies} contains only the trailer's
   * cookies.
   */
  void saveFromResponse(HttpUrl url, List<Cookie> cookies);

  /**
   * Load cookies from the jar for an HTTP request to {@code url}. This method returns a possibly
   * empty list of cookies for the network request.
   *
   * <p>Simple implementations will return the accepted cookies that have not yet expired and that
   * {@linkplain Cookie#matches match} {@code url}.
   */
  List<Cookie> loadForRequest(HttpUrl url);
}

```

- Cache类,缓存http,https的相应内容,以节省时间和流量

```
 /** <h3>Cache Optimization</h3>
 *
 * <p>To measure cache effectiveness, this class tracks three statistics:
 * <ul>
 *     <li><strong>{@linkplain #requestCount() Request Count:}</strong> the number of HTTP
 *         requests issued since this cache was created.
 *     <li><strong>{@linkplain #networkCount() Network Count:}</strong> the number of those
 *         requests that required network use.
 *     <li><strong>{@linkplain #hitCount() Hit Count:}</strong> the number of those requests
 *         whose responses were served by the cache.
 * </ul>
 */
```
CacheControl包含了缓存相关的http头参数
1. only-if-cached,force a cached response to be validated by the server
2. no-cache ,Force a Network Response
3. max-age=0,Force a Cache Response
4. max-stale

Cache类的重要属性 **DiskLruCache cache**,下面看下构造方法

```java
  public static DiskLruCache create(FileSystem fileSystem, File directory, int appVersion,
      int valueCount, long maxSize) {
    if (maxSize <= 0) {
      throw new IllegalArgumentException("maxSize <= 0");
    }
    if (valueCount <= 0) {
      throw new IllegalArgumentException("valueCount <= 0");
    }

    // Use a single background thread to evict entries.
    Executor executor = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>(), Util.threadFactory("OkHttp DiskLruCache", true));

    return new DiskLruCache(fileSystem, directory, appVersion, valueCount, maxSize, executor);
  }
```
FileSystem是个接口类型,只包含常量 **FileSystem SYSTEM**,这个单例通过 Okio进行文件访问.

```java
  FileSystem SYSTEM = new FileSystem() {
    @Override public Source source(File file) throws FileNotFoundException {
      return Okio.source(file);
    }

    @Override public Sink sink(File file) throws FileNotFoundException {
      try {
        return Okio.sink(file);
      } catch (FileNotFoundException e) {
        // Maybe the parent directory doesn't exist? Try creating it first.
        file.getParentFile().mkdirs();
        return Okio.sink(file);
      }
    }

    @Override public Sink appendingSink(File file) throws FileNotFoundException {
      try {
        return Okio.appendingSink(file);
      } catch (FileNotFoundException e) {
        // Maybe the parent directory doesn't exist? Try creating it first.
        file.getParentFile().mkdirs();
        return Okio.appendingSink(file);
      }
    }

    @Override public void delete(File file) throws IOException {
      // If delete() fails, make sure it's because the file didn't exist!
      if (!file.delete() && file.exists()) {
        throw new IOException("failed to delete " + file);
      }
    }
```
回到DiskLruCache创建,接下来创建了线程池,管理单个守候线程,守护线程是用来服务用户线程的，如果没有其他用户线程在运行，那么就没有可服务对象，也就没有理由继续下去。
```java
  DiskLruCache(FileSystem fileSystem, File directory, int appVersion, int valueCount, long maxSize,
      Executor executor) {
    this.fileSystem = fileSystem;
    this.directory = directory;
    this.appVersion = appVersion;
    this.journalFile = new File(directory, JOURNAL_FILE);
    this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
    this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
    this.valueCount = valueCount;
    this.maxSize = maxSize;
    this.executor = executor;
  }
```
三个重要文件
1. journalFile,文件内容如下

```
   /**     libcore.io.DiskLruCache
     *     1
     *     100
     *     2
     *
     *     CLEAN 3400330d1dfc7f3f7f4b8d4d803dfcf6 832 21054
     *     DIRTY 335c4c6028171cfddfbaae1a9c313c52
     *     CLEAN 335c4c6028171cfddfbaae1a9c313c52 3934 2342
     *     REMOVE 335c4c6028171cfddfbaae1a9c313c52
     *     DIRTY 1ab96a171faeeee38496d8b330771a7a
     *     CLEAN 1ab96a171faeeee38496d8b330771a7a 1600 234
     *     READ 335c4c6028171cfddfbaae1a9c313c52
     *     READ 3400330d1dfc7f3f7f4b8d4d803dfcf6
     */
```
第一行固定字符串libcore.io.DiskLruCache<br/>
第二行DiskLruCache的版本号，源码中为常量1<br/>
第三行为你的app的版本号，当然这个是你自己传入指定的<br/>
第四行指每个key对应几个文件，一般为1<br/>
第五行，空行<br/>

- InternalCache接口,具体实现在 **okhttp3.Cache.internalCache**,其实是调用Cache的方法

```java
public interface InternalCache {
  Response get(Request request) throws IOException;

  CacheRequest put(Response response) throws IOException;

  /**
   * Remove any cache entries for the supplied {@code request}. This is invoked when the client
   * invalidates the cache, such as when making POST requests.
   */
  void remove(Request request) throws IOException;

  /**
   * Handles a conditional request hit by updating the stored cache response with the headers from
   * {@code network}. The cached response body is not updated. If the stored response has changed
   * since {@code cached} was returned, this does nothing.
   */
  void update(Response cached, Response network) throws IOException;

  /** Track an conditional GET that was satisfied by this cache. */
  void trackConditionalCacheHit();

  /** Track an HTTP response being satisfied with {@code cacheStrategy}. */
  void trackResponse(CacheStrategy cacheStrategy);
}
```
get方法调用的是Cache的get方法
```java
  Response get(Request request) {
    String key = urlToKey(request);
    DiskLruCache.Snapshot snapshot;
    Entry entry;
    try {
      snapshot = cache.get(key);
      if (snapshot == null) {
        return null;
      }
    } catch (IOException e) {
      // Give up because the cache cannot be read.
      return null;
    }

    try {
      entry = new Entry(snapshot.getSource(ENTRY_METADATA));
    } catch (IOException e) {
      Util.closeQuietly(snapshot);
      return null;
    }

    Response response = entry.response(snapshot);

    if (!entry.matches(request, response)) {
      Util.closeQuietly(response.body());
      return null;
    }

    return response;
  }
```
urlTokey是将url转化为32个字符的十六进制串 *Util.md5Hex(request.url().toString());* ,通过缓存,重新封装成Response对象.


InternalCache方法,执行的也是Cache的put方法
```java
  private CacheRequest put(Response response) throws IOException {
    String requestMethod = response.request().method();

    if (HttpMethod.invalidatesCache(response.request().method())) {
      try {
        remove(response.request());
      } catch (IOException ignored) {
        // The cache cannot be written.
      }
      return null;
    }
    if (!requestMethod.equals("GET")) {
      // Don't cache non-GET responses. We're technically allowed to cache
      // HEAD requests and some POST requests, but the complexity of doing
      // so is high and the benefit is low.
      return null;
    }

    if (OkHeaders.hasVaryAll(response)) {
      return null;
    }

    Entry entry = new Entry(response);
    DiskLruCache.Editor editor = null;
    try {
      editor = cache.edit(urlToKey(response.request()));
      if (editor == null) {
        return null;
      }
      entry.writeTo(editor);
      return new CacheRequestImpl(editor);
    } catch (IOException e) {
      abortQuietly(editor);
      return null;
    }
  }
```
只缓存请求成功的Get请求. *cache.edit(urlToKey(response.request()));* 将获取或封装entry放入lruEntries对象 ,entry.writeTo(editor);使用的是Sink对象,写入缓存文件.


Cache还有个内部类 Entry
```java
    /**
     * Reads an entry from an input stream. A typical entry looks like this:
     * <pre>{@code
     *   http://google.com/foo
     *   GET
     *   2
     *   Accept-Language: fr-CA
     *   Accept-Charset: UTF-8
     *   HTTP/1.1 200 OK
     *   3
     *   Content-Type: image/png
     *   Content-Length: 100
     *   Cache-Control: max-age=600
     * }</pre>
     *
     * <p>A typical HTTPS file looks like this:
     * <pre>{@code
     *   https://google.com/foo
     *   GET
     *   2
     *   Accept-Language: fr-CA
     *   Accept-Charset: UTF-8
     *   HTTP/1.1 200 OK
     *   3
     *   Content-Type: image/png
     *   Content-Length: 100
     *   Cache-Control: max-age=600
     *
     *   AES_256_WITH_MD5
     *   2
     *   base64-encoded peerCertificate[0]
     *   base64-encoded peerCertificate[1]
     *   -1
     *   TLSv1.2
     * }</pre>
     */
  private static final class Entry {
    private final String url;
    private final Headers varyHeaders;
    private final String requestMethod;
    private final Protocol protocol;
    private final int code;
    private final String message;
    private final Headers responseHeaders;
    private final Handshake handshake;
    ...
    ...
    ...
  }
```

###Request创建
Request只有一个私有的构造方法，传入Request的内部类Builder
```java
      Request build = new Request.Builder().url("http://169.254.64.194/test/").build();
      private Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers.build();
        this.body = builder.body;
        this.tag = builder.tag != null ? builder.tag : this;
      }
```
看下Builder构造方法（okhttp3.Request.Builder）
```java
  public static class Builder {
    public Builder() {
      this.method = "GET";
      this.headers = new Headers.Builder();
    }

    private Builder(Request request) {
      this.url = request.url;
      this.method = request.method;
      this.body = request.body;
      this.tag = request.tag;
      this.headers = request.headers.newBuilder();
    }
    ···
    ···
  }
```
外部只能用Builder进行public构造方法初始化。设置请求方法为 *GET*，创建了Headers.Builderd对象。okhttp的构建者模式到现在已经用了三次，*Httpclient.Builder*,*Request.Builder*,*Headers.Builder*。看完构造够着方法，再看下 *Request.Builder*的属性。
```java
    private HttpUrl url;
    private String method;
    private Headers.Builder headers;
    private RequestBody body;
    private Object tag;
```
下面是一段Post请求，和Request.Builder对比起来，多了个tag，这个tag用来标记当前的Request.Builder。
```
    POST http://plugin.open.___.com/clientrequest/queryPluginByhostVersion.do HTTP/1.1
    Content-Length: 187
    Content-Type: application/x-www-form-urlencoded
    Host: plugin.open.___.com
    Connection: Keep-Alive
    User-Agent: Apache-HttpClient/UNAVAILABLE (java 1.4)

    sign=j9hiUV8ue%2F%2Ba%2F1tatjLdp0oCxZ0%3D&base=%7B%22agentId%22%3A%22100001%22%2C%22hostVersion%22%3A%221.0.0%22%2C%22osVersion%22%3A%2217%22%7D&buzz=%7B%22hostVersion%22%3A%221.0.0%22%7D
```
接着分析报文对应的属性。
-- HttpUrl
只有一个私有的构造方法
```java
  private HttpUrl(Builder builder) {
    this.scheme = builder.scheme;
    this.username = percentDecode(builder.encodedUsername, false);
    this.password = percentDecode(builder.encodedPassword, false);
    this.host = builder.host;
    this.port = builder.effectivePort();
    this.pathSegments = percentDecode(builder.encodedPathSegments, false);
    this.queryNamesAndValues = builder.encodedQueryNamesAndValues != null
        ? percentDecode(builder.encodedQueryNamesAndValues, true)
        : null;
    this.fragment = builder.encodedFragment != null
        ? percentDecode(builder.encodedFragment, false)
        : null;
    this.url = builder.toString();
  }
```
HttpUrl也是通过构造者模式创建，HttpUrl.Builder也是在HttpUrl的内部类，现在看下HttpUrl.Builder的属性和构造方法。先参考下通用的url格式
> scheme:[//[user:password@]host[:port]][/]path[?query][#fragment] [A generic URI is of the form](https://en.wikipedia.org/wiki/Uniform_Resource_Identifier#Generic_syntaxr)

```
                    hierarchical part
        ┌───────────────────┴─────────────────────┐
                    authority               path
        ┌───────────────┴───────────────┐┌───┴────┐
  abc://username:password@example.com:123/path/data?key=value#fragid1
  └┬┘   └───────┬───────┘ └────┬────┘ └┬┘           └───┬───┘ └──┬──┘
scheme  user information     host     port            query   fragment

  urn:example:mammal:monotreme:echidna
  └┬┘ └──────────────┬───────────────┘
scheme              path
```


```java
  public static final class Builder {
    String scheme;
    String encodedUsername = "";
    String encodedPassword = "";
    String host;
    int port = -1;
    final List<String> encodedPathSegments = new ArrayList<>();
    List<String> encodedQueryNamesAndValues;
    String encodedFragment;

    public Builder() {
      encodedPathSegments.add(""); // The default path is '/' which needs a trailing space.
    }
    ···
    ···
    ···
  }
```
支持通用的格式。
--- scheme只支持http,https
```java
    public Builder scheme(String scheme) {
      if (scheme == null) {
        throw new IllegalArgumentException("scheme == null");
      } else if (scheme.equalsIgnoreCase("http")) {
        this.scheme = "http";
      } else if (scheme.equalsIgnoreCase("https")) {
        this.scheme = "https";
      } else {
        throw new IllegalArgumentException("unexpected scheme: " + scheme);
      }
      return this;
    }
```
--- username,password连个处理方式一样，就以username作为分析
```java
    public Builder username(String username) {
      if (username == null) throw new IllegalArgumentException("username == null");
      this.encodedUsername = canonicalize(username, USERNAME_ENCODE_SET, false, false, false, true);
      return this;
    }

    public Builder encodedUsername(String encodedUsername) {
      if (encodedUsername == null) throw new IllegalArgumentException("encodedUsername == null");
      this.encodedUsername = canonicalize(
          encodedUsername, USERNAME_ENCODE_SET, true, false, false, true);
      return this;
    }
```
canonicalize方法的四个布尔类型参数如下
1. **alreadyEncoded** true to leave '%' as-is; false to convert it to '%25'.
2. **strict** true to encode '%' if it is not the prefix of a valid percent encoding.
3. **plusIsSpace** true to encode '+' as "%2B" if it is not already encoded.
4. **asciiOnly** true to encode all non-ASCII codepoints.

--- host
支持域名，IDN,ipv4,ipv6。下面是ipv6的格式
```http
    https://[2001:db8:85a3:8d3:1319:8a2e:370:7348]:443/
```
--- port 
支持到65539, http默认80，https默认443

-- Headers.builder
Headers的构建类，维护一个数组链表
```java
    private final List<String> namesAndValues = new ArrayList<>(20);
```
-- RequestBody
```java
  public static RequestBody create(MediaType contentType, String content) {
    Charset charset = Util.UTF_8;
    if (contentType != null) {
      charset = contentType.charset();
      if (charset == null) {
        charset = Util.UTF_8;
        contentType = MediaType.parse(contentType + "; charset=utf-8");
      }
    }
    byte[] bytes = content.getBytes(charset);
    return create(contentType, bytes);
  }
```
Request由多种静态方法创建，其中参数MediaType是对content type 的封装。
<br/>
到现在，简要分析了Request的初始化。
##添加请求
```java
        //异步
        Response response = client.newCall(request).execute();
        
        //同步
        okHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
```
看下OkHttpClient的newCall方法
```java
  @Override public Call newCall(Request request) {
    return new RealCall(this, request);
  }
```
RealCall关联重要的两个对象，即OkHttpClient和Request。<br/>
再往下看同步执行的RealCall的execute方法
```java
  @Override public Response execute() throws IOException {
    synchronized (this) {
      if (executed) throw new IllegalStateException("Already Executed");
      executed = true;
    }
    try {
      client.dispatcher().executed(this);
      Response result = getResponseWithInterceptorChain(false);
      if (result == null) throw new IOException("Canceled");
      return result;
    } finally {
      client.dispatcher().finished(this);
    }
  }
```
刚开始执行时，采用同步快，保证executed多线程访问。
<br/>然后使用OkHttpClient的调度对象，执行RealCall。最终添加到类型为ArrayDeque的runningSyncCalls容器中。关于ArrayDeque可以参考下AsyncTask。因为ArrayDeque非线程安全，所以加了同步块。在上面的方法执行到finally时候移除掉client.dispatcher().finished(this);
```java
  synchronized void executed(RealCall call) {
    runningSyncCalls.add(call);
  }
```
接着执行getResponseWithInterceptorChain方法，看起来使用责任链模式。
```java
  private Response getResponseWithInterceptorChain(boolean forWebSocket) throws IOException {
    Interceptor.Chain chain = new ApplicationInterceptorChain(0, originalRequest, forWebSocket);
    return chain.proceed(originalRequest);
  }
```
参数index等于0，Request请求，和是否是webSocket
```java

  class ApplicationInterceptorChain implements Interceptor.Chain {
    private final int index;
    private final Request request;
    private final boolean forWebSocket;

    ApplicationInterceptorChain(int index, Request request, boolean forWebSocket) {
      this.index = index;
      this.request = request;
      this.forWebSocket = forWebSocket;
    }
    ····
    ····
    ····
  }
```

执行proceed方法
```java
    @Override public Response proceed(Request request) throws IOException {
      // If there's another interceptor in the chain, call that.
      if (index < client.interceptors().size()) {
        Interceptor.Chain chain = new ApplicationInterceptorChain(index + 1, request, forWebSocket);
        Interceptor interceptor = client.interceptors().get(index);
        Response interceptedResponse = interceptor.intercept(chain);

        if (interceptedResponse == null) {
          throw new NullPointerException("application interceptor " + interceptor
              + " returned null");
        }

        return interceptedResponse;
      }

      // No more interceptors. Do HTTP.
      return getResponse(request, forWebSocket);
    }
  }
```
先是查询是否有拦截器，现在暂时不考虑。则走getResponse方法。
```java
```

##缓存策略
Cache和InternalCache

##连接池
> 外部网络请求的入口都是通过Transport接口来完成。该类采用了桥接模式将HttpEngine和HttpConnection来连接起来。

ConnectionPool,缓存RealConnection,RealConnection封装socket
```
```
##重连机制

RouteSelector,在StreamAllocation构造方法初始化.HttpEngine调用StreamAllocation的newStream方法时候,最终会走到RouteSelector
```java
  private HttpStream connect() throws RouteException, RequestException, IOException {
    boolean doExtensiveHealthChecks = !networkRequest.method().equals("GET");
    return streamAllocation.newStream(client.connectTimeoutMillis(),
        client.readTimeoutMillis(), client.writeTimeoutMillis(),
        client.retryOnConnectionFailure(), doExtensiveHealthChecks);
  }
```
StreamAllocation包含了Connections,Streams,Calls三者的关系
```java
 /**
 * <ul>
 *     <li><strong>Connections:</strong> physical socket connections to remote servers. These are
 *         potentially slow to establish so it is necessary to be able to cancel a connection
 *         currently being connected.
 *     <li><strong>Streams:</strong> logical HTTP request/response pairs that are layered on
 *         connections. Each connection has its own allocation limit, which defines how many
 *         concurrent streams that connection can carry. HTTP/1.x connections can carry 1 stream
 *         at a time, SPDY and HTTP/2 typically carry multiple.
 *     <li><strong>Calls:</strong> a logical sequence of streams, typically an initial request and
 *         its follow up requests. We prefer to keep all streams of a single call on the same
 *         connection for better behavior and locality.
 * </ul>
 */
```

##Gzip的使用方式
RequestBodyCompression

Gzip实现的方式主要是通过GzipSink对普通sink的封装压缩。

##安全性

java的SSLSocket相关的开发再来了解本框架中的安全性
##平台适应性

Platform是整个平台适应的核心类。同时它封装了针对不同平台的三个平台类Android和JdkWithJettyBootPlatform。




``` java
  001_initial_mockwebserver 0c4790dafaf6ccf7d8d34d04d24aac65cac910eb Initial checkin of MockWebServer.
  002_ant_maven             4ebac2656c870a090141106ad9ce267d25863301 Upgrade from ant to maven.
  003_initial_client        ea63463c2ff8a67f63e541036f0d5998b5bb1f3e Initial import.

                              google mockwebserver
                +-------------------------------------------------------+
                |                  HttpConnectionPool                   |
                |                           +---------------------------+
                |                           |    bouncycastle           |
                |                           |    conscrypt              |
                |                           |    JettyNpnProvider       |
                |                           |    HttpsEngine            |
                +---------------------------+---------------------------+
                |  HttpEngine                                           |
                |     responseCache                                     |
                |     transparentGzip                                   |
                +---------------------------+---------------------------+
                |  HttpURLConnectionImpl    |    HttpsURLConnectionImpl |
                |           proxy           |            HttpsEngine    |
                |           redirectionCount+---------------------------+
                |           httpEngine      |    OkHttpsConnection      |
                +---------------------------+---------------------------+
                |                    OkHttpConnection                   |
                +-------------------------------------------------------+
                |                     URLConnection                     |
                +-------------------------------------------------------+
 
  100_parent1.0.0           d95ecff5423f19e019e178baddfe4211f2fe57aa [maven-release-plugin] prepare release parent-1.0.0
                +-------------------------------------------------------+
                |                  HttpConnectionPool                   |
                |                           +---------------------------+
                |                           |    Platform               |
                |                           |    HttpsEngine            |
                +---------------------------+---------------------------+
                |  HttpEngine                                           |
                |     responseCache                                     |
                |     transparentGzip                                   |
                +---------------------------+---------------------------+
                |  HttpURLConnectionImpl    |    HttpsURLConnectionImpl |
                |           proxy           |            HttpsEngine    |
                |           redirectionCount+---------------------------+
                |           httpEngine      |    OkHttpsConnection      |
                +---------------------------+---------------------------+
                |                    OkHttpConnection                   |
                +-------------------------------------------------------+
                |                     URLConnection                     |
                +-------------------------------------------------------+
                |                     OkHttpClient                      |
                +-------------------------------------------------------+
 
* 200_parent-2.0.0-RC1      8dcc74d339e0664580756063ff47c65c6f1a17ae [maven-release-plugin] prepare release parent-2.0.0-RC1 
* 300_parent-3.0.0-RC1      ffc35dbd02822bf6584c6144266cbbca6b348b17 [maven-release-plugin] prepare release parent-3.0.0-RC1
  400_parent-4.0.0-ALPHA01  8f21b934f928986bba7e50114911c3c494e1d5c5 Prepare for release 4.0.0-ALPHA01.

                gradle+kotlin


```


OKIO 缓存字节，避免空间浪费，避免频繁GC（zero-fill and GC churn ）
```
* 001_okbuffer 2d3cdc9e5dfb593f8eb7da38459163b902a95d7f OkBuffer API sketch.
                +--------------------------------------------+
                |           ByteString                       |
                +--------------------------------------------+
                |                            SegmentPool     |
                +--------------------------------------------+
                |         Segment                            |
                |             pos//for read                  |
                |             limite // for write            |
                +--------------------------------------------+
                |         OKBuffer:Sink,Source               |
                |                segment                     |
                |                byteCount                   |
                +--------------------------------------------+

  002_okbuffer_dapter       bece94f8970fa6d0615581f81f71efa4b950d3b2 Adapters for sources and sinks.
  003_InflaterSource        88b2592d0aea290aecd38bc24a9e3ed66bb38d05 InflaterSource. Like InflaterInputStream.
  004_GipSource             8efe816e6fa54efd935b08050dfdcf43a50f8bbe GzipSource beginnings.
  005_BufferedSink          d1dcbcf786fff9f1d1da0ffea6703e2f749c032b BufferedSink.
  006_DeflaterSink          2fcbac79930492eeb379b9c9b3c63cca4e8789df DeflaterSink.
  007_RealBufferedSink      c287d63829969fe03f559e76deb735c8286d4d32 Interfaces for BufferedSource and BufferedSink.
  008_GzipSink              d0a461ffd277ab8e34edd7ca995a0fae0b02b790 Add GzipSink for writing Gzip data.
  050_okio-parent0.5.0      21362a2634b287f3d2136327a56ebf1b9738a7ae [maven-release-plugin] prepare release okio-parent-0.5.0
                maven


* 200_okio_parent-2.0.0-RC1 4da4d9e3e311cdce7294fc59507f00dca2cf2ce0 Update changelog for 2.0.0-RC1
                Gradle+Kotlin


```