package me.didi.api.ess.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String API_TITLE = "ESS - Escola Shining Star API";
    private static final String API_DESCRIPTION = "API for registering and controlling students, courses, classes, registrations and grades.";
    private static final String API_VERSION = "0.0.1-SNAPSHOT";
    private static final String API_LICENSE = "Apache-2.0";
    private static final String API_LICENSE_URL = "https://www.apache.org/licenses/LICENSE-2.0";
    private static final String CONTACT_NAME = "Luis Zancanela";
    private static final String CONTACT_GITHUB = "https://github.com/didifive";
    private static final String CONTACT_EMAIL = "luisczdidi@gmail.com";

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .version(API_VERSION)
                        .description(API_DESCRIPTION)
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().
                                name(API_LICENSE).
                                url(API_LICENSE_URL))
                        .contact(new Contact()
                                .email(CONTACT_EMAIL)
                                .name(CONTACT_NAME)
                                .url(CONTACT_GITHUB)));
    }
}
