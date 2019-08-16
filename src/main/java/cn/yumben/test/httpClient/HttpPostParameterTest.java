package cn.yumben.test.httpClient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class HttpPostParameterTest {

    public static void main(String[] args) throws Exception {
        //HttpClients对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //设置请求地址
        URIBuilder uriBuilder = new URIBuilder("http://120.79.213.36/2.6/web/pages/UI.php");
        HttpPost httpPost = null;
        //创建HttpGet对象，设置请求参数
        httpPost = new HttpPost(uriBuilder.build());
        //声明List集合，封装表单中的参数
        List<NameValuePair> param   =new ArrayList<NameValuePair>();
        //operation=details&class=DocumentFile&id=37&c[menu]=Document
        param.add(new BasicNameValuePair("user","张志刚"));
        param.add(new BasicNameValuePair("pwd","zzg521213145"));

        //创建表单Entity对象，第一个参数是封装好的表单参数，第二个参数是编码
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(param, "utf8");
        //设置表单里的Entity对象到Post请求中
        httpPost.setEntity(formEntity);

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
