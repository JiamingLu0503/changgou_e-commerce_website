package com.changgou.goods;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient  // start up Eureka Client
@MapperScan(basePackages = {"com.changgou.goods.dao"})  // scan mapper package
public class GoodsApplication {

    /**
     * load starter class and configure springboot with the starter class
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }
}
