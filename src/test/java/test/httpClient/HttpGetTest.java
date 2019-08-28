package test.httpClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpGetTest {

    public static void main(String[] args) throws URISyntaxException {
        //HttpClients对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置请求地址
        URIBuilder uriBuilder = new URIBuilder("https://github.com/apache/kafka");
        HttpGet httpGet = null;
        //创建HttpGet对象，设置请求参数
        httpGet = new HttpGet(uriBuilder.build());

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
