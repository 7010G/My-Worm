# My-Worm

本项目会对网络已公布的漏洞数据进行数据收集，可以根据用户指定的产品名称和版本号，收集与之匹配的数据，并自动生成漏洞报告。

数据来源：http://www.cnnvd.org.cn

使用方法：启动项目->发送请求:
#获取Tomcat7.0.2漏洞报告
http://localhost:8080/getData?name=Tomcat&version=7.0.2

#获取配置文件内的产品信息并扫描漏洞
http://localhost:8080/getData

默认配置文件为：
src/main/resources/Product.json

项目集成了IK分词器，词库配置文件为：
src / main / resources / ext.dic


报告图列：
![image](https://user-images.githubusercontent.com/37602564/112410576-5bb19280-8d56-11eb-8c8e-722f25e8174e.png)
