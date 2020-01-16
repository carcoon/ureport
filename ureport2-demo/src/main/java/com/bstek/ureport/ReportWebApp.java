package com.bstek.ureport;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author lqf
 */
@ImportResource("classpath:ureport-console-context.xml")
@Configuration
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@SpringBootApplication
@ServletComponentScan

public class ReportWebApp {


    public static void main(String[] args) {
        SpringApplication.run(ReportWebApp.class, args);
    }
}
