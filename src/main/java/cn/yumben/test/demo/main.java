package cn.yumben.test.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class main {

    private static  List<BugReport> bugReportsList = new ArrayList<BugReport>();

    public static void main(String[] args) throws InterruptedException {


        postTest();


        //HttpClientUtil.imageDownload("https://img12.360buyimg.com/n7/jfs/t1/79706/18/6581/112584/5d492587E32ba70ae/47fdb0779e7dad8a.jpg");

    }

    /**
     * 获取初始信息完成自动翻页
     */
    public static void postTest() throws InterruptedException {

        ArrayList<String> productNameList = new ArrayList<>();
        productNameList.add("Tomcat");
        productNameList.add("Zookeeper");
        productNameList.add("Nginx");
        String url = "http://www.cnnvd.org.cn/web/vulnerability/queryLds.tag";

        for (String productName : productNameList) {
            //线程一解析详情页链接
            Runnable run_1 = new Runnable() {
                @Override
                public void run() {
                    HashMap<String, String> paramMap = new HashMap<>();
                    paramMap.put("qcvCname", productName);
                    String postRequest = new HttpClientUtil().postRequest(url, paramMap);
                    //自动翻页,返回每一页的报告详情链接
                    List<String> detailsUrlList = new JsoupUtil().parseString(postRequest, url, paramMap);
                    //拆分详情页链接
                    List<List<String>> split = ListDeal.split(detailsUrlList, 5);

                    for (int i = 0; i < split.size(); i++) {
                        int finalI = i;
                        //线程二解析详情页数据实体对象
                        Runnable run_2 = new Runnable() {
                            @Override
                            public void run() {
                                List<BugReport> bugReports = new JsoupUtil().parsedetailsUrl(split.get(finalI));
                                for (BugReport bugReport : bugReports) {
                                    bugReportsList.add(bugReport);
                                    //我无法知道还有没有其它线程正在运行
                                    System.out.println(bugReportsList.size());
                                }
                            }
                        };
                        TheThreadPool.getThreadPool().execute(run_2);
                    }
                }
            };
            TheThreadPool.getThreadPool().execute(run_1);
        }
       /* while (true) {
            *//*if (Thread.activeCount()==1) {
                System.out.println("结束了！");
                TheThreadPool.getThreadPool().shutdown();
                break;
            }*//*
            System.out.println(bugReportsList.size());
            Thread.sleep(2000);
        }*/

    }
}
