package cn.yumben.test.demo;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientPool {

    private static PoolingHttpClientConnectionManager  connectionManager=null;

    public static PoolingHttpClientConnectionManager getConnectionManager(){

        synchronized (HttpClientPool.class){

            if(connectionManager ==null ){
                //创建连接池管理器
                connectionManager=new PoolingHttpClientConnectionManager();
                //设置最大连接数
                connectionManager.setMaxTotal(200);
                //设置每个主机的最大连接数
                connectionManager.setDefaultMaxPerRoute(200);
            }
        }
        return  connectionManager;
    }

}
