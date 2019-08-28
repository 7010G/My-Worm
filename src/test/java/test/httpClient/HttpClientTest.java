package test.httpClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpClientTest {

    public static void main(String[] args) throws URISyntaxException {
        //HttpClients对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置请求地址
        URIBuilder uriBuilder = new URIBuilder("https://github.com/apache/kafka");
        //设置参数，如有多个参数就连续setParameter
        uriBuilder.setParameter("", "");
        HttpGet httpGet = null;
        //创建HttpGet对象，设置请求参数
        httpGet = new HttpGet(uriBuilder.build());

        //配置请求信息
        RequestConfig requestConfig = RequestConfig.custom()
                //设置创建连接的最长时间，单位是毫秒
                .setConnectTimeout(1000)
                //设置获取连接的最长时间，单位是毫秒
                .setConnectionRequestTimeout(500)
                //设置数据传输的最长时间，单位是毫秒
                .setSocketTimeout(10 * 1000).build();
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse execute = null;
        try {
            //使用HttpClients发起请求，获取Response
            execute = httpClient.execute(httpGet);
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
                execute.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
