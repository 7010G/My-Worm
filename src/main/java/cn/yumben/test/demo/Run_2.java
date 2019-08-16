package cn.yumben.test.demo;

import java.util.ArrayList;
import java.util.List;

public class Run_2 implements Runnable {

    private  List<String> detailsUrlList  = null;
    public static List<BugReport> bugReportsList=new ArrayList<BugReport>();


    public Run_2(List<String> detailsUrlList) {
        this.detailsUrlList = detailsUrlList;
    }

    /**
     * 多线程解析被拆分的详情页
     */
    @Override
    public void run() {
        List<BugReport> bugReports = new JsoupUtil().parsedetailsUrl(detailsUrlList);
        for (BugReport bugReport :bugReports){
            bugReportsList.add(bugReport);
        }
        System.err.println(bugReportsList.size());
    }
}
