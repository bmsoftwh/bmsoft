
package com.bmsoft.gateway.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/***
 * 资源配置
 */
@Component
@Primary
public class SwaggerResourceConfig implements SwaggerResourcesProvider {

    @Autowired
    RouteLocator routeLocator;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private GatewayProperties gatewayProperties;

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;
    
    private Logger log = LoggerFactory.getLogger(getClass());

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public List<SwaggerResource> get() {
        String url = "/swagger-resources";
        //获取所有router
        List<SwaggerResource> resources = new ArrayList<>();

        List<String> routes = new ArrayList<>();
        
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        gatewayProperties.getRoutes().stream()
                .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(route -> {
                    route.getPredicates().stream()
                            .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                            .forEach(predicateDefinition -> {
                                        try {
                                            JSONArray list = restTemplate.getForObject("http://" + route.getUri().getHost() + url, JSONArray.class);
                                            if (!list.isEmpty()) {
                                                for (int i = 0; i < list.size(); i++) {
                                                    SwaggerResource sr = list.getObject(i, SwaggerResource.class);
                                                    resources.add(swaggerResource(route.getId() + "-" + sr.getName(), "/" + route.getId() + sr.getUrl()));
//                                                    resources.add(swaggerResource(route.getId() + "-" + sr.getName(), contextPath + "/" + route.getId() + sr.getUrl()));
                                                }
                                            }
                                        } catch (Exception e) {
                                            log.warn("加载后端资源时失败{}", route.getUri().getHost());
                                        }
                                    }

                            );
                });

        //gateway 网关和bootsway
//        resources.add(swaggerResource("网关模块", "/v2/api-docs?group=网关模块"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        log.info("name:{},location:{}", name, location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
