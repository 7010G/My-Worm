package cn.yumben.test.demo;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    private static HashMap<String, ArrayList<BugReport>> hashMap = new HashMap<>();
    /**
     * 初始化产品集
     */
    private static JSONArray productNameList = ConfigUtil.getValues("SZS", "ProductList");

    private final static Logger logger = LoggerFactory.getLogger(main.class);

    public static void main(String[] args) throws InterruptedException {
        Date a = new Date();
        postTest();
        Date b = new Date();
        System.out.println(("运行耗时:" + (b.getTime() - a.getTime()) / 1000) + "s");
    }

    /**
     * 获取初始信息完成自动翻页
     */
    public static void postTest() throws InterruptedException {
        String url = "http://www.cnnvd.org.cn/web/vulnerability/queryLds.tag";

        //使用Lambda表达式实现多线程闭包
        for (int i = 0; i < productNameList.length(); i++) {
            String productName = productNameList.get(i).toString();

            hashMap.put(productName, new ArrayList<BugReport>());
            //解析详情页链接
            Runnable run_1 = () -> run_1(productName, url);
            TheThreadPool.getThreadPool().execute(run_1);

        }
        while (true) {
            int queueSize = TheThreadPool.getThreadPool().getQueue().size();
            logger.info("当前排队线程数：" + queueSize);

            int activeCount = TheThreadPool.getThreadPool().getActiveCount();
            logger.info("当前活动线程数：" + activeCount);

            long completedTaskCount = TheThreadPool.getThreadPool().getCompletedTaskCount();
            logger.info("执行完成线程数：" + completedTaskCount);

            long taskCount = TheThreadPool.getThreadPool().getTaskCount();
            logger.info("总线程数：" + taskCount);


            Thread.sleep(1000);
            if (TheThreadPool.getThreadPool().getActiveCount() == 0) {
                TheThreadPool.getThreadPool().shutdown();
                for (int a = 0; a < productNameList.length(); a++) {
                    logger.info(productNameList.get(a).toString());
                }
                break;
            }
        }
        dataSet();
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
            logger.info("当前排队线程数：" + queueSize);
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
                Runnable run_2 = () -> run_2(split.get(finalI), productName);
                TheThreadPool.getThreadPool().execute(run_2);
            }
        }
    }

    /**
     * 解析详情页数据实体对象
     *
     * @param split 被平均拆分的部分详情链接
     */
    public static void run_2(List<String> split, String productName) {

        List<BugReport> bugReports = new JsoupUtil().parsedetailsUrl(split);
        for (BugReport bugReport : bugReports) {
            hashMap.get(productName).add(bugReport);
        }
    }
    /**
     * 将结果集数据与配置文件数据进行对比返回匹配项
     */
    public static void dataSet() {
        //总数
        int sun = 0;
        //有效集
        int effectiveSet = 0;
        //最终结果
        ArrayList<BugReport> resultfinal = new ArrayList<>();
        //遍历产品集
        for (Object jsonObject : productNameList) {
            //根据产品名称获取对用产品的版本集
            JSONArray versionArray = ConfigUtil.getJSONObject()
                    .getJSONObject("SZS")
                    .getJSONObject("ProductVersion")
                    .getJSONArray(jsonObject.toString());
            //根据产品名称获取单个产品的漏洞集
            ArrayList<BugReport> arrayList = hashMap.get(jsonObject.toString());
            //循环对比漏洞信息
            for (BugReport bugReport : arrayList) {
                System.err.println(bugReport.getLoopholeSynopsis());
                //漏洞简介
                String loopholeSynopsis = bugReport.getLoopholeSynopsis();
                //产品版本
                for (Object versionObject : versionArray) {
                        try {
                            List<String> participleList = ParticipleUtil.getParticipleList(loopholeSynopsis);
                            boolean code = ParticipleUtil.matchVersion(versionObject.toString(), participleList);
                            if (code) {
                                resultfinal.add(bugReport);
                                effectiveSet++;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
                sun++;
            }
        }
        /**
         *  观察漏洞报告可得知，除去版本 硬匹配得到的数据之外 版本号附近 包含 " 及之前 ， 至 ，  之前 "这几个字段代表：
         *
         *   xxxx及之前 代表小于等于xxxx版本,   aaaa至bbbb 代表 大于等于 aaaa版本小于等于 bbbb版本  ，aaa之前  代表小于aaa版本
         */
        logger.info("漏洞总数：" + sun + "，匹配漏洞数：" + effectiveSet);
        for (BugReport bugReport:resultfinal){
            logger.info(bugReport.toString());
        }

    }
}
