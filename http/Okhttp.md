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