package cn.yumben.test.demo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 调优需要注意的有 产品的总数量不能超过线程排队列队大小  单个产品的详情链接不能超过线程排队列队大小 拆分的详情页链接多少链接为一个线程
 *
 * @author zzg
 */
public class main {

    private static List<BugReport> bugReportsList = new ArrayList<BugReport>();
    private static Date a = new Date();

    public static void main(String[] args) throws InterruptedException {


        postTest();
        //HttpClientUtil.imageDownload("https://img12.360buyimg.com/n7/jfs/t1/79706/18/6581/112584/5d492587E32ba70ae/47fdb0779e7dad8a.jpg");
    }

    /**
     * 获取初始信息完成自动翻页
     */
    public static void postTest() throws InterruptedException {
        String url = "http://www.cnnvd.org.cn/web/vulnerability/queryLds.tag";
        JSONArray productNameList = ConfigUtil.getValues("SZS", "ProductList");
        System.out.println();
        //使用Lambda表达式实现多线程闭包
        for (int i = 0; i < productNameList.length(); i++) {

            //解析详情页链接
            int finalI = i;
            Runnable run_1 = () -> run_1(productNameList.get(finalI).toString(), url);
            TheThreadPool.getThreadPool().execute(run_1);

        }
        while (true) {
            int queueSize = TheThreadPool.getThreadPool().getQueue().size();
            System.err.println("当前排队线程数：" + queueSize);

            int activeCount = TheThreadPool.getThreadPool().getActiveCount();
            System.err.println("当前活动线程数：" + activeCount);

            long completedTaskCount = TheThreadPool.getThreadPool().getCompletedTaskCount();
            System.err.println("执行完成线程数：" + completedTaskCount);

            long taskCount = TheThreadPool.getThreadPool().getTaskCount();
            System.err.println("总线程数：" + taskCount);

            Thread.sleep(1000);
            if (TheThreadPool.getThreadPool().getActiveCount() == 0) {
                TheThreadPool.getThreadPool().shutdown();
                for (BugReport bugReport : bugReportsList) {
                    System.out.println(bugReport.toString());
                }
                for (int a = 0; a < productNameList.length(); a++) {
                    System.out.println(productNameList.get(a));
                }
                System.out.println("漏洞总数：" + bugReportsList.size());
                Date b = new Date();
                System.out.println(("运行耗时:" + (b.getTime() - a.getTime()) / 1000) + "s");
                break;
            }
        }
    }

    /**
     * 解析详情页链接
     *
     * @param productName 查询条件
     * @param url         爬取地址
     */
    public static void run_1(String productName, String url) {

        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("qcvCname", productName);
        String postRequest = new HttpClientUtil().postRequest(url, paramMap);
        //自动翻页,返回每一页的报告详情链接
        List<String> detailsUrlList = new JsoupUtil().parseString(postRequest, url, paramMap);
        //拆分详情页链接
        List<List<String>> split = ListDeal.split(detailsUrlList, 5);

        for (int i = 0; i < split.size(); i++) {
            int queueSize = TheThreadPool.getThreadPool().getQueue().size();
            System.err.println("当前排队线程数：" + queueSize);
            //如果当前排队线程数等于 等待列队的大小，则休眠1s
            if (queueSize == TheThreadPool.CAPACITY) {
                try {
                    i -= 1;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                int finalI = i;
                Runnable run_2 = () -> run_2(split.get(finalI));
                TheThreadPool.getThreadPool().execute(run_2);
            }
        }
    }

    /**
     * 解析详情页数据实体对象
     *
     * @param split 被平均拆分的部分详情链接
     */
    public static void run_2(List<String> split) {
        List<BugReport> bugReports = new JsoupUtil().parsedetailsUrl(split);
        for (BugReport bugReport : bugReports) {
            bugReportsList.add(bugReport);
        }
    }

    /**
     * 将结果集数据与配置文件数据进行对比返回匹配项
     */
    public static void dataSet() {

    }
}
