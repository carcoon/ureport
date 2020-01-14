package com.bstek.ureport.report.provider;

import com.bstek.ureport.console.UReportServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyServletConfig {
    //注册Servlet
    @Bean
    public ServletRegistrationBean buildUreportServlet(){
        return new ServletRegistrationBean(new UReportServlet(), "/ureport/*");
    }
}
