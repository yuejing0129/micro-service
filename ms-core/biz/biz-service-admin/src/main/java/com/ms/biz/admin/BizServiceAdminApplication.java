package com.ms.biz.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.DispatcherType;

import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.module.comm.xss.XssFilter;

/**
 * 在Servlet容器中部署WAR的时候，不能依赖于Application的main函数而是要以类似于web.xml文件配置的方式来启动Spring应用上下文<br/>
 * 所以此时需要声明这样一个类或者将应用的主类改为继承SpringBootServletInitializer也可以
 * @author yuejing
 * @date 2017年2月21日 下午2:42:38
 */
@EnableDiscoveryClient
@ComponentScan("com.*")
@SpringBootApplication
public class BizServiceAdminApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BizServiceAdminApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BizServiceAdminApplication.class);
    }
	

	@Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new XssFilter());
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
        return registrationBean;
    }
	
	/**
	 * 处理静态资源
	 * @return
	 */
	@Bean
    public FilterRegistrationBean expiresFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        ExpiresFilter expiresFilter = new ExpiresFilter();
        registrationBean.setFilter(expiresFilter);
        int time = 6 * 60;
        registrationBean.addInitParameter("ExpiresByType image", "access plus " + time + " minutes");
        registrationBean.addInitParameter("ExpiresByType text/css", "access plus " + time + " minutes");
        registrationBean.addInitParameter("ExpiresByType text/javascript", "access plus " + time + " minutes");
        registrationBean.addInitParameter("ExpiresByType application/javascript", "access plus " + time + " minutes");
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setOrder(0);
        registrationBean.setUrlPatterns(urlPatterns);
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }
}
