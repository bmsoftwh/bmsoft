package com.bmsoft.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

//@Component
//@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {

	/**
     * swagger的api json文档路径
     */
    public static final String API_URI = "/doc.html";
    /**
     * Nacos发现功能的方法的名字，注册的服务会加入这个前缀
     */
    public static final String NACOS_SUB_PFIX = "ReactiveCompositeDiscoveryClient_";
    
    /**
             * 服务发现的路由处理器
     */
    private final DiscoveryClientRouteDefinitionLocator routeLocator;
    
    private final static String URL = "/swagger-resources";
    
    @Autowired
    RestTemplate restTemplate;
    

    public SwaggerProvider(DiscoveryClientRouteDefinitionLocator routeLocator) {
        this.routeLocator = routeLocator;
    }
    
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public List<SwaggerResource> get() {

        List<SwaggerResource> resources = new ArrayList<>();

        //List<String> routes = new ArrayList<>();
        //从DiscoveryClientRouteDefinitionLocator 中取出routes，构造成swaggerResource
        routeLocator.getRouteDefinitions().subscribe(route -> {
            route.getPredicates().stream()
            .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
            .forEach(predicateDefinition -> {
            	///
            	
            	String name = route.getId();
            	name = name.substring(NACOS_SUB_PFIX.length());
            	resources.add(swaggerResource(name,
                    predicateDefinition.getArgs().get("pattern").replace("/**", API_URI)));
                    
            	
            });
            
        });
        
        return resources;
    }
    private SwaggerResource swaggerResource(String name, String location) {

        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
