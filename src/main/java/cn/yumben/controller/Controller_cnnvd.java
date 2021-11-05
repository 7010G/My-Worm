package cn.yumben.controller;

import cn.yumben.pojo.BugReport;
import cn.yumben.service.Service_cnnvd;
import cn.yumben.service.Service_cnnvd_interface;
import cn.yumben.util.DateTool;
import cn.yumben.util.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Copyright © 2018yunben. All rights reserved.
 * <p>
 *
 * @Description: TODO
 * 2019/8/30/13:57
 * @author: ZZG
 * @version: 1.0
 */
@Controller
@Scope("prototype")
public class Controller_cnnvd {

    @Resource(name = "service_cnnvd")
    private Service_cnnvd_interface service_cnnvd_interface;

    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    @ResponseBody
    public String showData(String name, String version,String dis, HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException {
        //if(null==name&&null==version){
        //  return "参数为空";
        // }else {
        List<BugReport> bugReports = service_cnnvd_interface.postTest(name, version,dis);
        List<Map<String, String>> result = new ArrayList<>();
        for (BugReport bugReport : bugReports) {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("loopholeName", bugReport.getLoopholeName());
            map.put("cnnvdId", bugReport.getCnnvdId());
            map.put("importantLevel", bugReport.getImportantLevel());
            map.put("cveId", bugReport.getCveId());
            map.put("loopholeClass", bugReport.getLoopholeClass());
            map.put("publishTime", bugReport.getPublishTime());
            map.put("threatenClass", bugReport.getThreatenClass());
            map.put("updateTime", bugReport.getUpdateTime());
            map.put("manufacturers", bugReport.getManufacturers());
            map.put("source", bugReport.getSource());
            map.put("loopholeSynopsis", bugReport.getLoopholeSynopsis());
            map.put("hitVersion", bugReport.getHitVersion());
            result.add(map);
        }
        if (null == name) {
            name = "默认配置";
            version = "All";
        }
        String date = DateTool.formatDate(new Date());
        String fileName = (name + version + "漏洞报告" + date);        // 定义文件名
        String headString = name + ":" + version + "漏洞报告";          // 定义表格标题
        String sheetName = name;                  // 定义工作表表名
        String filePath = "E:\\test\\";             // 文件本地保存路径
        String[] thead = {"漏洞名称", "CNNVD编号", "危害等级", "CVE编号", "漏洞类型", "发布时间", "威胁类型", "更新时间", "厂商", "漏洞来源", "漏洞简介","命中版本"};                    // 定义表头内容
        int[] sheetWidth = {2500, 3500, 3000, 4000, 2500, 5000, 5000, 5000, 5000, 5000, 21000,2500};   // 定义每一列宽度
        HSSFWorkbook wb = new HSSFWorkbook();           // 创建Excel文档对象
        HSSFSheet sheet = wb.createSheet(sheetName);    // 创建工作表
        // ①创建表格标题
        ExcelUtil.createHeadTittle(wb, sheet, headString, result.get(0).size() - 1);
        // result.get(0).size() - 1为表格占用列数，从0开始
        // ②创建表头
        ExcelUtil.createThead(wb, sheet, thead, sheetWidth);
        // ③填入数据
        ExcelUtil.createTable(wb, sheet, result);
        //FileOutputStream fos = new FileOutputStream(new File(filePath + fileName));
        // filePath,fileName是如上定义的文件保存路径及文件名
        OutputStream outputStream = response.getOutputStream();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");//设置contentType为excel格式
        String userAgent = request.getHeader("USER-AGENT");
        if (StringUtils.contains(userAgent, "MSIE")) {//IE浏览器
            fileName = URLEncoder.encode(fileName, "UTF8");
        } else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
            fileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF8");//其他浏览器
        }
        response.setCharacterEncoding("UTF-8");
        URLEncoder.encode(fileName, "gb2312");
        response.setHeader("Content-Disposition", "Attachment;Filename=" + fileName + ".xls");
        //定义下载的类型，标明是excel文件
        response.setContentType("application/vnd.ms-excel");
        wb.write(outputStream);
        //fos.close();
        outputStream.close();
        wb.close();
        return "OK";
        //}
    }

    @RequestMapping(value = "/versionVS", method = RequestMethod.POST)
    @ResponseBody
    public boolean versionVS(String loopholeSynopsis, String version) {

        boolean b = service_cnnvd_interface.versionVS(loopholeSynopsis, version);
        return b;
    }

}
