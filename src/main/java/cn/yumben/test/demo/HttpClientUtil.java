package cn.yumben.test.demo;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.*;

public class HttpClientUtil {


    private static RequestConfig requestConfig = null;


    //初始化HttpClient和请求配置信息
    static {
        requestConfig = RequestConfig.custom()
                //设置创建连接的最长时间，单位是毫秒
                .setConnectTimeout(5000)
                //设置获取连接的最长时间，单位是毫秒
                .setConnectionRequestTimeout(5000)
                //设置数据传输的最长时间，单位是毫秒
                .setSocketTimeout(10 * 1000).build();
    }

    /**
     * get请求
     *
     * @param url
     * @param param
     * @return
     */
    public String getRequest(String url, HashMap<String, String> param) {
        CloseableHttpClient HttpClient = HttpClients.custom().setConnectionManager(HttpClientPool.getConnectionManager()).build();
        //请求返回实体
        String content = null;
        try {
            //配置请求地址和请求参数
            URIBuilder uriBuilder = new URIBuilder(url);
            if (param != null) {
                if (param.size() != 0) {
                    for (Map.Entry<String, String> entry : param.entrySet()) {
                        uriBuilder.setParameter(entry.getKey(), entry.getValue());
                    }
                }
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            //配置请求信息
            httpGet.setConfig(requestConfig);
            //发起请求并解析响应
            content = parseTheResponse(httpGet, null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * post请求
     *
     * @param url
     * @param param
     * @return
     */
    public String postRequest(String url, HashMap<String, String> param) {
        CloseableHttpClient HttpClient = HttpClients.custom().setConnectionManager(HttpClientPool.getConnectionManager()).build();
        //请求返回实体
        String content = null;
        //声明List集合，封装表单中的参数
        List<NameValuePair> paramList = null;
        try {
            //配置请求地址和请求参数
            URIBuilder uriBuilder = new URIBuilder(url);
            if (param != null) {
                if (param.size() != 0) {
                    paramList = new ArrayList<NameValuePair>();
                    for (Map.Entry<String, String> entry : param.entrySet()) {
                        paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                }
            }
            //创建表单Entity对象，第一个参数是封装好的表单参数，第二个参数是编码
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(paramList, "utf8");
            HttpPost httpPost = new HttpPost(uriBuilder.build());
            //配置请求信息
            httpPost.setConfig(requestConfig);
            //设置表单里的Entity对象到Post请求中
            httpPost.setEntity(formEntity);
            //设置请求头
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36");
            //发起请求并解析响应
            content = parseTheResponse(null, httpPost);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 解析响应
     *
     * @param httpGet
     * @param httpPost
     * @return
     * @throws IOException
     */
    private String parseTheResponse(HttpGet httpGet, HttpPost httpPost) throws IOException {
        CloseableHttpClient HttpClient = HttpClients.custom().setConnectionManager(HttpClientPool.getConnectionManager()).build();
        String content = null;
        CloseableHttpResponse execute = null;
        if (httpGet != null) {
            execute = HttpClient.execute(httpGet);
        } else {
            execute = HttpClient.execute(httpPost);
        }
        //解析响应
        if (execute.getStatusLine().getStatusCode() == 200) {
            if (execute.getEntity() != null) {
                HttpEntity entity = execute.getEntity();
                content = EntityUtils.toString(entity, "utf8");
            }
        }

        if (execute != null) {
            execute.close();
        }
        return content;
    }

    /**
     * 下载图片
     *
     * @param url
     * @return
     */
    public boolean imageDownload(String url) {
        CloseableHttpClient HttpClient = HttpClients.custom().setConnectionManager(HttpClientPool.getConnectionManager()).build();
        CloseableHttpResponse execute = null;
        //配置请求地址和请求参数
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(url);
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            //配置请求信息
            httpGet.setConfig(requestConfig);
            execute = HttpClient.execute(httpGet);
            //解析响应
            if (execute.getStatusLine().getStatusCode() == 200) {
                if (execute.getEntity() != null) {
                    //获取图片后缀
                    String extName = url.substring(url.lastIndexOf('.'));
                    //创建图片名
                    String imgName = UUID.randomUUID().toString() + extName;
                    //声明OutPutStream
                    OutputStream fileOutputStream = new FileOutputStream("C:\\Users\\14020\\Desktop\\imgs\\" + imgName);
                    execute.getEntity().writeTo(fileOutputStream);

                } else {
                    return false;
                }
            }
            if (execute != null) {
                execute.close();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
