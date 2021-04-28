package cn.yumben.service;

import cn.yumben.ServiceApplication;
import cn.yumben.common.TheThreadPool;
import cn.yumben.pojo.BugReport;
import cn.yumben.util.Cnnvd.JsoupUtil;
import cn.yumben.util.Cnnvd.ParticipleUtil;
import cn.yumben.util.ConfigUtil;
import cn.yumben.util.HttpClientUtil;
import cn.yumben.util.ListDeal;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright © 2018yunben. All rights reserved.
 * <p>
 *
 * @Description: TODO
 * 2019/8/27/17:41
 * @author: ZZG
 * @version: 1.0
 */
@Service
@Scope("prototype")
public class Service_cnnvd implements Service_cnnvd_interface {

    private HashMap<String, ArrayList<BugReport>> hashMap = new HashMap<>();

    /**
     * 初始化产品集
     */
    private JSONArray productNameList = null;

    private Logger logger = LoggerFactory.getLogger(ServiceApplication.class);

    private String NAME = null;
    private String VERSION = null;

    /**
     * 获取初始信息完成自动翻页
     *
     * @param name    产品名称
     * @param version 产品版本
     * @param dis json文件后缀,用来切换不同json配置文件
     * @return
     * @throws InterruptedException
     */
    public List<BugReport> postTest(String name, String version, String dis) throws InterruptedException {
        String url = "http://www.cnnvd.org.cn/web/vulnerability/queryLds.tag";
        if (null != name && null != version) {
            NAME = name;
            VERSION = version;
            hashMap.put(NAME, new ArrayList<BugReport>());
            //解析详情页链接
            Runnable run_1 = () -> run_1(NAME, url);
            TheThreadPool.getThreadPool().execute(run_1);
            while (true) {
                int queueSize = TheThreadPool.getThreadPool().getQueue().size();
                logger.info("当前排队线程数：" + queueSize);

                int activeCount = TheThreadPool.getThreadPool().getActiveCount();
                logger.info("当前活动线程数：" + activeCount);

                long completedTaskCount = TheThreadPool.getThreadPool().getCompletedTaskCount();
                logger.info("执行完成线程数：" + completedTaskCount);

                long taskCount = TheThreadPool.getThreadPool().getTaskCount();
                logger.info("总线程数：" + taskCount);


                Thread.sleep(5000);
                if (TheThreadPool.getThreadPool().getActiveCount() == 0) {
                    //TheThreadPool.getThreadPool().shutdown();
                /*for (int a = 0; a < productNameList.length(); a++) {
                    logger.info(productNameList.get(a).toString());
                }*/
                    break;
                }
            }
        } else {
            if(null!=dis){
                ConfigUtil.DIS=dis;
                ConfigUtil.load();
            }
            productNameList = ConfigUtil.getValues("dbase", "ProductList");

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
                    break;
                }
            }
        }

        List<BugReport> bugReports = dataSet();
        return bugReports;
    }

    /**
     * 解析详情页链接
     *
     * @param productName 查询条件
     * @param url         爬取地址
     */
    public void run_1(String productName, String url) {

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
     * @param split       被平均拆分的部分详情链接
     * @param productName 产品名称
     */
    public void run_2(List<String> split, String productName) {

        List<BugReport> bugReports = new JsoupUtil().parsedetailsUrl(split);
        for (BugReport bugReport : bugReports) {
            hashMap.get(productName).add(bugReport);
        }
    }

    /**
     * 将结果集数据与配置文件数据进行对比返回匹配项
     */
    public List<BugReport> dataSet() {
        //总数
        int sun = 0;
        //有效集
        int effectiveSet = 0;
        //最终结果
        ArrayList<BugReport> resultfinal = new ArrayList<>();
        //遍历产品集
        if (productNameList != null) {

            for (Object jsonObject : productNameList) {
                //根据产品名称获取对用产品的版本集
                JSONArray versionArray = ConfigUtil.getJSONObject()
                        .getJSONObject("dbase")
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
                        if (versionVS(loopholeSynopsis, versionObject.toString())) {
                            resultfinal.add(bugReport);
                            effectiveSet++;
                        }
                    }
                    sun++;
                }
            }
        } else {
            //根据产品名称获取单个产品的漏洞集
            ArrayList<BugReport> arrayList = hashMap.get(NAME);
            //循环对比漏洞信息
            for (BugReport bugReport : arrayList) {
                System.err.println(bugReport.getLoopholeSynopsis());
                //漏洞简介
                String loopholeSynopsis = bugReport.getLoopholeSynopsis();
                //产品版本
                if (versionVS(loopholeSynopsis, VERSION)) {
                    resultfinal.add(bugReport);
                    effectiveSet++;
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
        for (BugReport bugReport : resultfinal) {
            logger.info(bugReport.toString());
        }
        NAME = null;
        VERSION = null;
        return resultfinal;
    }

    /**
     * 匹配版本号
     *
     * @return
     */
    public boolean versionVS(String loopholeSynopsis, String version) {

        boolean code = false;
        ParticipleUtil participleUtil = new ParticipleUtil();
        try {
            List<String> participleList = participleUtil.getParticipleList(loopholeSynopsis);
            code = participleUtil.matchVersion(version, participleList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }
}
