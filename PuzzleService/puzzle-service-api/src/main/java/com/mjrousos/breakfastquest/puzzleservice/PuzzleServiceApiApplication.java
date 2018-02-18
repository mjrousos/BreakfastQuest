package com.mjrousos.breakfastquest.puzzleservice;

import java.util.ArrayList;

import javax.servlet.Filter;

import com.microsoft.applicationinsights.web.internal.WebRequestTrackingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class PuzzleServiceApiApplication {

    @Value("${appName}")
    private String appName;

	public static void main(String[] args) {
		SpringApplication.run(PuzzleServiceApiApplication.class, args);
    }

    // Register filters so that we can add the App Insights filter
    @Bean
    public FilterRegistrationBean registerFilters() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(appInsightsWebRequestTrackingFilter());
        registration.addUrlPatterns("/*");
        registration.setName("webRequestTrackingFilter");
        registration.setOrder(1);
        return registration;
    }

    // Register App Insights filter
    @Bean(name = "appInsightsWebRequestTrackingFilter")
    public Filter appInsightsWebRequestTrackingFilter() {
        WebRequestTrackingFilter filter =  new WebRequestTrackingFilter(appName);
        return filter;
    }

    // Register Swagger configuration
    @Bean
    public Docket swaggerApi() {
    	Contact apiContact = new Contact("Mike Rousos", "http://www.BreakfastQuest.com", "BreakfastQuestApi@MJRousos.com");
    	ApiInfo apiInfo = new ApiInfo(appName,"REST API for creating, retrieving, updating, deleting, and solving Breakfast Quest puzzles.", "0.1.0", null, apiContact, null, null, new ArrayList<VendorExtension>());
        return new Docket(DocumentationType.SWAGGER_2)
    	  .apiInfo(apiInfo)
    	  .tags(new Tag("Puzzle Controller", "Create, delete, update, view, and solve puzzles"))
          .select()
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
          .build();
    }
}
