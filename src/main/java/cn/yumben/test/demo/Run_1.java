package cn.yumben.test.demo;

import java.util.HashMap;
import java.util.List;

public class Run_1 implements Runnable {

    private String productName = null;
    private String url = null;


    public Run_1(String productName, String url) {
        this.productName = productName;
        this.url = url;
    }

    /**
     * 多线程查询各个产品的详情连接，并行性拆分
     */
    @Override
    public void run() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("qcvCname",productName);
        String postRequest = new HttpClientUtil().postRequest(url, paramMap);
        //自动翻页,返回每一页的报告详情链接
        List<String> detailsUrlList = new JsoupUtil().parseString(postRequest, url, paramMap);
        //拆分详情页连接
        List<List<String>> split = ListDeal.split(detailsUrlList, 5);

        //解析各个报告详情页生成实体对象
        for(int i =0 ;i < split.size();i++){
            Run_2 run_2 = new Run_2(split.get(i));
            TheThreadPool.getThreadPool().execute(run_2);
        }
        //List<BugReport> vulnerabilities = new JsoupUtil().parsedetailsUrl(detailsUrlList);
        /*for (BugReport bugReport :vulnerabilities){
            System.out.println(bugReport.toString());
        }*/

    }
}
