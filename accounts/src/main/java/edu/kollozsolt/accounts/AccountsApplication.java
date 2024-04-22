package edu.kollozsolt.accounts;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
        info = @Info(
                title = "Accounts microservice REST API Documentation",
                description = "EasyBank Accounts microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Kollo Zsolt",
                        email = "kollo.zsolt@codespring.ro",
                        url = "https://google.com"
                ),
                license = @License(
                    name = "Apache 2.0",
                    url = "https://google.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "EasyBank Accounts microservice REST API Documentation",
                url = "https://google.com"
        )
)
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
