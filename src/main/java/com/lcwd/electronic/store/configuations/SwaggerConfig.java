package com.lcwd.electronic.store.configuations;





/*
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Electronic Store Application")
                        .description("This is Backend Project created by Aayush Prajapat")
                        .version("1.0.0V"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}*/


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customizeOpenAPI() {
        final String securitySchemeName = "Jwt Token";

        return new OpenAPI()
                .info(new Info()
                        .title("Electronic Store Application")
                        .description("This is a backend project created by Aayush Prajapat. It serves as the backend for an electronic store application, built with Spring Boot, providing secure and efficient APIs for managing products, orders, and user accounts.")
                        .version("1.0.0V")
                        .termsOfService("https://www.aayushprajapat.com")
                        .contact(new Contact()
                                .name("Aayush Prajapat")
                                .url("https://instagram.com/__aayush_.03")
                                .email("aayushprajapat49@gmail.com"))
                        .license(new License()
                                .name("License of APIs")
                                .url("https://www.aayushprajapat.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}




/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
	
//	getApiInfo() ek method hai orr ishka return type ApiInfo honga
	
	
	@Bean
	public Docket docket() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		docket.apiInfo(getApiInfo());
		
		//If we are Implementing the Security with swagger then we have to set security context and security schemes
		 docket.securityContexts(Arrays.asList(getSecurityContext()));
	        docket.securitySchemes(Arrays.asList(getSchemes()));

//		hum apni apis ko customize bhi kr skte hai kon kon si apies batani hai
		ApiSelectorBuilder select = docket.select();
		select.apis(RequestHandlerSelectors.any());
		select.paths(PathSelectors.any());
		Docket builDocket = select.build();
		return builDocket;
	}
	
	  private SecurityContext getSecurityContext() {

	        SecurityContext context = SecurityContext
	                .builder()
	                .securityReferences(getSecurityReferences())
	                .build();
	        return context;
	    }

	    private List<SecurityReference> getSecurityReferences() {
	        AuthorizationScope[] scopes = {new AuthorizationScope("Global", "Access Every Thing")};
	        return Arrays.asList(new SecurityReference("JWT", scopes));

	    }

	    private ApiKey getSchemes() {
	        return new ApiKey("JWT", "Authorization", "header");
	    }

	
	private ApiInfo getApiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"Electronic Store Application",
				"This is Backend Project created by Aayush Prajapt",
				"1.0.0V",
				"https://www.aayushprajapat.com",
				new Contact("Aayush Prajapat","https://instagram.com/__aayush_.03","aayushprajapat49@gmail.com"),
				"License of APIs",
				"https://www.aayushprajapat.com",
				new ArrayList<>()
				);
		return apiInfo;
		
	}
	
	

}*/
