package cn.yumben.test.httpClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpPostTest {

    public static void main(String[] args) throws URISyntaxException {
        //HttpClients对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置请求地址
        URIBuilder uriBuilder = new URIBuilder("https://github.com/apache/kafka");
        HttpPost httpPost = null;
        //创建HttpGet对象，设置请求参数
        httpPost = new HttpPost(uriBuilder.build());

        CloseableHttpResponse execute = null;
        try {
            //使用HttpClients发起请求，获取Response
            execute = httpClient.execute(httpPost);
            //解析响应
            if (execute.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = execute.getEntity();
                String content = EntityUtils.toString(entity, "utf8");
                System.out.println(content);


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (execute!=null){
                    execute.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
