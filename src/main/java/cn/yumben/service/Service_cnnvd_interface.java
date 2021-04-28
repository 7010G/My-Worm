package cn.yumben.service;

import cn.yumben.pojo.BugReport;

import java.util.List;

public interface Service_cnnvd_interface {

    List<BugReport> postTest(String name, String version,String dis) throws InterruptedException;

    boolean versionVS(String loopholeSynopsis, String version);

}
