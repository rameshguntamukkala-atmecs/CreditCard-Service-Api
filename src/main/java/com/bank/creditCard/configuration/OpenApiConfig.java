package com.bank.creditCard.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
 @Bean
 public OpenAPI customOpenAPI() {
  return new OpenAPI().components(new Components())
    .info(new Info().title("Credit Card Service API").description(
      "Credit Card Service API is a service which provides "
      + "credit cards to users and maintains all the data and "
      + "transaction details of a credit card. "
      + "By using this system a customer can view his credit card(s) details "
      + "and can verify all the transaction details done by his/her card(s)."));
 }
}
