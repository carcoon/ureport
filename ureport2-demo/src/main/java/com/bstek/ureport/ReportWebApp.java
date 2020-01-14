package com.bstek.ureport;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lqf
 */
@ImportResource("classpath:ureport-console-context.xml")

//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@SpringBootApplication
@ServletComponentScan

public class ReportWebApp {

    public static void main(String[] args) {
        SpringApplication.run(ReportWebApp.class, args);
    }
}
