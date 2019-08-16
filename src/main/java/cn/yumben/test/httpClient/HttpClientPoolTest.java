package cn.yumben.test.httpClient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpClientPoolTest {

    public static void main(String[] args) throws URISyntaxException {
        //创建连接池管理器
        PoolingHttpClientConnectionManager clientConnectionManager =new PoolingHttpClientConnectionManager();
        //设置最大连接数
        clientConnectionManager.setMaxTotal(100);
        //设置每个主机的最大连接数
        clientConnectionManager.setDefaultMaxPerRoute(10);
        doGet(clientConnectionManager);
        doGet(clientConnectionManager);

    }

    private static void doGet(PoolingHttpClientConnectionManager clientConnectionManager) throws URISyntaxException {
        CloseableHttpClient HttpClient = HttpClients.custom().setConnectionManager(clientConnectionManager).build();

        HttpGet httpGet = new HttpGet("https://baidu.com");

        CloseableHttpResponse response=null;
        try {
             response = HttpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode()==200){

                String content = EntityUtils.toString(response.getEntity(), "utf8");
                System.out.println(content);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
