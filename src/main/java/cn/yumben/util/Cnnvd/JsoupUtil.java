package cn.yumben.util.Cnnvd;

import cn.yumben.pojo.BugReport;
import cn.yumben.util.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 解析www.Cnnvd.org.cn JsoupUtil工具类
 * @author zzg
 */
public class JsoupUtil {

    /**
     * 自动翻页,返回每一页的报告详情链接
     *
     * @param content
     */
    public  List<String> parseString(String content, String url, HashMap paramMap) {
        //下一页链接
        String nextPage = null;
        //存储详情页链接
        ArrayList<String> detailsUrl = new ArrayList<String>();
        //解析字符串
        Document document = Jsoup.parse(content);
        //由于总页数取不到，只能选择总条数来计算总页数
        Element select = document.select("[onmouse='']").first();
        String count = select.text();
        System.out.println("产品名称:"+paramMap.get("qcvCname"));
        //获取数据总条数
        Integer pagCount = Integer.parseInt(count.substring(count.lastIndexOf("：") + 1).replace(",",""));
        System.out.println("总条数:"+pagCount);
        //计算总页数 （数据总条数+每页显示的条数-1）/每页显示的条数
        int numberPage = (pagCount + 10 - 1) / 10;
        System.out.println("总页数:" + numberPage);
        //遍历每一页
        for (int i = 1; i <= numberPage; i++) {
            nextPage = url + "?pageno=" + i;
            //开始解析每页数据
            String postRequest = new HttpClientUtil().postRequest(nextPage, paramMap);
            //首先收集每页数据的详情页链接
            Document parse = Jsoup.parse(postRequest);
            Elements select1 = parse.select("[target=_blank].a_title2");
            for (Element element : select1) {
                //服务器地址+请求地址
                detailsUrl.add("http://www.cnnvd.org.cn" + element.attr("href"));
            }
        }
        /*for (String str : detailsUrl) {
           System.out.println(str);
        }*/
        return detailsUrl;
    }

    /**
     * 解析各个报告详情页生成实体对象
     *
     * @return
     */
    public  List<BugReport> parsedetailsUrl(List<String> parsedetailsUrl) {
        ArrayList<BugReport> bugReportList = new ArrayList<BugReport>();
        for (String url : parsedetailsUrl) {
            BugReport bugReport = new BugReport();
            String request = new HttpClientUtil().getRequest(url, null);
            //解析页面
            Document document = Jsoup.parse(request);
            //获取漏洞名称
            String loopholeName = document.select("[class=detail_xq w770] >h2").text();
            bugReport.setLoopholeName(loopholeName);
            System.out.println(loopholeName);
            //获取漏洞详情
            Elements particulars = document.select("[class=detail_xq w770] > ul > li");
            /*System.err.println(particulars);*/
            for (Element element : particulars) {
                String content = element.text();
               /* System.out.println(content);*/
                //获取CNNVD编号
                if (content.contains("CNNVD")) {
                    bugReport.setCnnvdId(content.substring(content.lastIndexOf("：") + 1));
                } else if (content.contains("危害等级")) {
                    bugReport.setImportantLevel(content.substring(content.lastIndexOf("：") + 1));
                } else if (content.contains("CVE")) {
                    bugReport.setCveId(content.substring(content.lastIndexOf("：") + 1));
                } else if (content.contains("漏洞类型")) {
                    bugReport.setLoopholeClass(content.substring(content.lastIndexOf("：") + 1));
                } else if (content.contains("发布时间")) {
                    bugReport.setPublishTime(content.substring(content.lastIndexOf("：") + 1));
                } else if (content.contains("威胁类型")) {
                    bugReport.setThreatenClass(content.substring(content.lastIndexOf("：") + 1));
                } else if (content.contains("更新时间")) {
                    bugReport.setUpdateTime(content.substring(content.lastIndexOf("：") + 1));
                } else if (content.contains("厂 商")) {
                    bugReport.setManufacturers(content.substring(content.lastIndexOf("：") + 1));
                }else if (content.contains("漏洞来源")) {
                    bugReport.setSource(content.substring(content.lastIndexOf("：") + 1));
                }
            }
            //获取漏洞简介
            String loopholeSynopsis = document.select("[class=d_ldjj] > p").text();
            /*System.out.println("漏洞简介:"+loopholeSynopsis);*/
            bugReport.setLoopholeSynopsis(loopholeSynopsis);
            //获取受影响的实体
            Elements affectedEntities = document.select("[id=ent] > p");
            ArrayList<String> affectedEntitiesList = new ArrayList<String>();
            for (Element element : affectedEntities) {
                /*System.out.println("受影响的实体:"+element.text());*/
                affectedEntitiesList.add(element.text());
            }
            bugReport.setAffectedEntities(affectedEntitiesList);
            //获取补丁
            Elements patchs = document.select("[id=pat]");
            HashMap<String, String> patchMap = new HashMap<String, String>();
            for(Element element:patchs){
                Elements as = element.getElementsByTag("a");
              /* System.out.println("补丁:"+as);*/
                for(Element a: as ) {
                    patchMap.put(a.text(), "http://www.cnnvd.org.cn"+a.attr("href"));
                }
            }
            bugReport.setPatch(patchMap);

            bugReportList.add(bugReport);
        }
        return bugReportList;
    }

}
