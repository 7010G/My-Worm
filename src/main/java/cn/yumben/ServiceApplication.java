package cn.yumben;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 调优需要注意的有 产品的总数量不能超过线程排队列队大小  单个产品的详情链接不能超过线程排队列队大小 拆分的详情页链接多少链接为一个线程
 *
 * @author zzg
 */
@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

}
