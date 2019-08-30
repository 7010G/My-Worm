package cn.yumben.controller;

import cn.yumben.pojo.BugReport;
import cn.yumben.service.Service_cnnvd;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

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
public class Controller_cnnvd {
    @Resource
    Service_cnnvd service_cnnvd;

    @RequestMapping(value = "/getDate", method = RequestMethod.GET)
    @ResponseBody
    public String showData(String name, String version) throws InterruptedException {

        List<BugReport> bugReports = service_cnnvd.postTest(name, version);
        return bugReports.toString();
    }


}
