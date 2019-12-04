package cn.yumben.common;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author zzg
 */
public class HttpClientPool {

    private static PoolingHttpClientConnectionManager connectionManager = null;
    private static CloseableHttpClient httpClient =null;

    public static PoolingHttpClientConnectionManager getConnectionManager() {

        synchronized (HttpClientPool.class) {

            if (connectionManager == null) {
                //创建连接池管理器
                connectionManager = new PoolingHttpClientConnectionManager();
                //设置最大连接数
                connectionManager.setMaxTotal(1000);
                //设置每个主机的最大连接数
                connectionManager.setDefaultMaxPerRoute(500);
            }
        }
         //httpClient = HttpClients.createDefault();
        return connectionManager;
    }

}
