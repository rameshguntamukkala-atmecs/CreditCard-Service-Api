package com.bank.creditCard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableScheduling
//@EnableSwagger2
public class CreditCardApplication {
 public static void main(String[] args) {
  SpringApplication.run(CreditCardApplication.class, args);
 }
}
