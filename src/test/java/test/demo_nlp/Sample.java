package test.demo_nlp;

import com.baidu.aip.nlp.AipNlp;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright © 2018yunben. All rights reserved.
 * <p>
 *
 * @Description: TODO
 * 2019/8/23/16:02
 * @author: ZZG
 * @version: 1.0
 */
public class Sample {
    //设置APPID/AK/SK
    public static final String APP_ID = "17072092";
    public static final String API_KEY = "8p2fNZ5RVF2NyQMIGK39Hqwh";
    public static final String SECRET_KEY = "6gDKkW7nAGdrhRqp0dAjzWjmLv5L7dQX";

    public static void main(String[] args) throws JSONException {
        // 初始化一个AipNlp
        AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
       // client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 调用接口
        String text = "ApacheTomcat是美国阿帕奇（Apache）软件基金会的一款轻量级Web应用服务器。该程序实现了对Servlet和\n" +
                "JavaServerPage（JSP）的支持。ApacheTomcat9.0.0.M1版本至9.0.0.17版本、8.5.0版本至8.5.39版本和\n" +
                "7.0.0版本至7.0.93版本中存在跨站脚本漏洞。该漏洞源于WEB应用缺少对客户端数据的正确验证。攻击者可利用该漏洞\n" +
                "执行客户端代码。";
        JSONObject res = client.lexer(text, null);
        System.out.println(res.toString(2));

    }
}
