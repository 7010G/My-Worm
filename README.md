# My-Worm

本项目会对网络已公布的漏洞数据进行数据收集，可以根据用户指定的产品名称和版本号，收集与之匹配的数据，并自动生成漏洞报告。

数据来源：http://www.cnnvd.org.cn

使用方法：启动项目->发送请求:
#获取Tomcat7.0.2漏洞报告
http://localhost:8080/getData?name=Tomcat&version=7.0.2

#获取LZ漏洞报告
http://localhost:8080/getData?dis=LZ

#获取SZS漏洞报告
http://localhost:8080/getData?dis=SZS

#获取配置文件内的产品信息并扫描漏洞
http://localhost:8080/getData

#调用匹配接口进行匹配(Post请求/versionVS)如下例
key1:loopholeSynopsis==( Apache Tomcat是美国阿帕奇（Apache）软件基金会的一款轻量级Web应用服务器。该程序实现了对Servlet和JavaServer Page（JSP）的支持。 Apache Tomcat 9.0.0.M1版本至9.0.0.17版本、8.5.0版本至8.5.39版本和7.0.0-7.0.93版本中存在跨站脚本漏洞。该漏洞源于WEB应用缺少对客户端数据的正确验证。攻击者可利用该漏洞执行客户端代码。)
key2:version==(7.0.1)

return true||false

默认配置文件为：
src/main/resources/Product_SZS.json

项目集成了IK分词器，词库配置文件为：
src / main / resources / ext.dic


报告图列：
![image](https://user-images.githubusercontent.com/37602564/112410576-5bb19280-8d56-11eb-8c8e-722f25e8174e.png)
