package edu.hrbeu.myweather;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class HttpRequestManager {
    public final String HTTP_GET = "GET";

    public final String HTTP_POST = "POST";

    /**
     * 当前请求的 URL
     */
    protected String url = "";

    /**
     * HTTP 请求的类型
     */
    protected String requsetType = HTTP_GET;

    /**
     * 连接请求的超时时间
     */
    protected int connectionTimeout = 5000;

    /**
     * 读取远程数据的超时时间
     */
    protected int soTimeout = 10000;

    /**
     * 服务端返回的状态码
     */
    protected int statusCode = -1;

    /**
     * 当前链接的字符编码
     */
    protected String charset = HTTP.UTF_8;

    /**
     * HTTP GET 请求管理器
     */
    protected HttpRequestBase httpRequest = null;

    /**
     * HTTP 请求的配置参数
     */
    protected HttpParams httpParameters = null;

    /**
     * HTTP 请求响应
     */
    protected HttpResponse httpResponse = null;

    /**
     * HTTP 客户端连接管理器
     */
    protected HttpClient httpClient = null;

    /**
     * HTTP POST 方式发送多段数据管理器
     */
    protected MultipartEntityBuilder multipartEntityBuilder = null;

    /**
     * 绑定 HTTP 请求的事件监听器
     */
    protected OnHttpRequestListener onHttpRequestListener = null;

    public HttpRequestManager() {
    }

    public HttpRequestManager(OnHttpRequestListener listener) {
        this.setOnHttpRequestListener(listener);
    }

    /**
     * 设置当前请求的链接
     *
     * @param url
     * @return
     */
    public HttpRequestManager setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout 单位（毫秒），默认 5000
     * @return
     */
    public HttpRequestManager setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
        return this;
    }

    /**
     * 设置 socket 读取超时时间
     *
     * @param timeout 单位（毫秒），默认 10000
     * @return
     */
    public HttpRequestManager setSoTimeout(int timeout) {
        this.soTimeout = timeout;
        return this;
    }

    /**
     * 设置获取内容的编码格式
     *
     * @param charset 默认为 UTF-8
     * @return
     */
    public HttpRequestManager setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 获取当前 HTTP 请求的类型
     *
     * @return
     */
    public String getRequestType() {
        return this.requsetType;
    }

    /**
     * 判断当前是否 HTTP GET 请求
     *
     * @return
     */
    public boolean isGet() {
        return this.requsetType == HTTP_GET;
    }

    /**
     * 判断当前是否 HTTP POST 请求
     *
     * @return
     */
    public boolean isPost() {
        return this.requsetType == HTTP_POST;
    }

    /**
     * 获取 HTTP 请求响应信息
     *
     * @return
     */
    public HttpResponse getHttpResponse() {
        return this.httpResponse;
    }

    /**
     * 获取 HTTP 客户端连接管理器
     *
     * @return
     */
    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    /**
     * 添加一条 HTTP 请求的 header 信息
     *
     * @param name
     * @param value
     * @return
     */
    public HttpRequestManager addHeader(String name, String value) {
        this.httpRequest.addHeader(name, value);
        return this;
    }

    /**
     * 获取 HTTP GET 控制器
     *
     * @return
     */
    public HttpGet getHttpGet() {
        return (HttpGet) this.httpRequest;
    }

    /**
     * 获取 HTTP POST 控制器
     *
     * @return
     */
    public HttpPost getHttpPost() {
        return (HttpPost) this.httpRequest;
    }

    /**
     * 获取请求的状态码
     *
     * @return
     */
    public int getStatusCode() {
        return this.statusCode;
    }

    /**
     * 通过 GET 方式请求数据
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String get(String url) throws Exception {
        this.requsetType = HTTP_GET;
        // 设置当前请求的链接
        this.setUrl(url);
        // 新建 HTTP GET 请求
        this.httpRequest = new HttpGet(this.url);
        // 执行客户端请求
        this.httpClientExecute();
        // 监听服务端响应事件并返回服务端内容
        return this.checkStatus();
    }

    /**
     * 获取 HTTP POST 多段数据提交管理器
     *
     * @return
     */
    public MultipartEntityBuilder getMultipartEntityBuilder() {
        if (this.multipartEntityBuilder == null) {
            this.multipartEntityBuilder = MultipartEntityBuilder.create();
            // 设置为浏览器兼容模式
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            // 设置请求的编码格式
            multipartEntityBuilder.setCharset(Charset.forName(this.charset));
        }
        return this.multipartEntityBuilder;
    }

    /**
     * 配置完要 POST 提交的数据后, 执行该方法生成数据实体等待发送
     */
    public void buildPostEntity() {
        // 生成 HTTP POST 实体
        HttpEntity httpEntity = this.multipartEntityBuilder.build();
        this.getHttpPost().setEntity(httpEntity);
    }

    /**
     * 发送 POST 请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public String post(String url) throws Exception {
        this.requsetType = HTTP_POST;
        // 设置当前请求的链接
        this.setUrl(url);
        // 新建 HTTP POST 请求
        this.httpRequest = new HttpPost(this.url);
        // 执行客户端请求
        this.httpClientExecute();
        // 监听服务端响应事件并返回服务端内容
        return this.checkStatus();
    }

    /**
     * 执行 HTTP 请求
     *
     * @throws Exception
     */
    protected void httpClientExecute() throws Exception {
        // 配置 HTTP 请求参数
        this.httpParameters = new BasicHttpParams();
        this.httpParameters.setParameter("charset", this.charset);
        // 设置 连接请求超时时间
        HttpConnectionParams.setConnectionTimeout(this.httpParameters, this.connectionTimeout);
        // 设置 socket 读取超时时间
        HttpConnectionParams.setSoTimeout(this.httpParameters, this.soTimeout);
        // 开启一个客户端 HTTP 请求
        this.httpClient = new DefaultHttpClient(this.httpParameters);
        // 启动 HTTP POST 请求执行前的事件监听回调操作(如: 自定义提交的数据字段或上传的文件等)
        this.getOnHttpRequestListener().onRequest(this);
        // 发送 HTTP 请求并获取服务端响应状态
        this.httpResponse = this.httpClient.execute(this.httpRequest);
        // 获取请求返回的状态码
        this.statusCode = this.httpResponse.getStatusLine().getStatusCode();
    }

    /**
     * 读取服务端返回的输入流并转换成字符串返回
     *
     * @throws Exception
     */
    public String getInputStream() throws Exception {
        // 接收远程输入流
        InputStream inStream = this.httpResponse.getEntity().getContent();
        // 分段读取输入流数据
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        // 数据接收完毕退出
        inStream.close();
        // 将数据转换为字符串保存
        return new String(baos.toByteArray(), this.charset);
    }

    /**
     * 关闭连接管理器释放资源
     */
    protected void shutdownHttpClient() {
        if (this.httpClient != null && this.httpClient.getConnectionManager() != null) {
            this.httpClient.getConnectionManager().shutdown();
        }
    }

    /**
     * 监听服务端响应事件并返回服务端内容
     *
     * @return
     * @throws Exception
     */
    protected String checkStatus() throws Exception {
        OnHttpRequestListener listener = this.getOnHttpRequestListener();
        String content;
        if (this.statusCode == HttpStatus.SC_OK) {
            // 请求成功, 回调监听事件
            content = listener.onSucceed(this.statusCode, this);
        } else {
            // 请求失败或其他, 回调监听事件
            content = listener.onFailed(this.statusCode, this);
        }
        // 关闭连接管理器释放资源
        this.shutdownHttpClient();
        return content;
    }

    /**
     * HTTP 请求操作时的事件监听接口
     */
    public interface OnHttpRequestListener {
        /**
         * 初始化 HTTP GET 或 POST 请求之前的 header 信息配置 或 其他数据配置等操作
         *
         * @param request
         * @throws Exception
         */
        public void onRequest(HttpRequestManager request) throws Exception;

        /**
         * 当 HTTP 请求响应成功时的回调方法
         *
         * @param statusCode 当前状态码
         * @param request
         * @return 返回请求获得的字符串内容
         * @throws Exception
         */
        public String onSucceed(int statusCode, HttpRequestManager request) throws Exception;

        /**
         * 当 HTTP 请求响应失败时的回调方法
         *
         * @param statusCode 当前状态码
         * @param request
         * @return 返回请求失败的提示内容
         * @throws Exception
         */
        public String onFailed(int statusCode, HttpRequestManager request) throws Exception;
    }

    /**
     * 绑定 HTTP 请求的监听事件
     *
     * @param listener
     * @return
     */
    public HttpRequestManager setOnHttpRequestListener(OnHttpRequestListener listener) {
        this.onHttpRequestListener = listener;
        return this;
    }

    /**
     * 获取已绑定过的 HTTP 请求监听事件
     *
     * @return
     */
    public OnHttpRequestListener getOnHttpRequestListener() {
        return this.onHttpRequestListener;
    }
}