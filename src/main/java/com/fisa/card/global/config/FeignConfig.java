package com.fisa.card.global.config;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.fisa.card.openfeign") // Feign 클라이언트 인터페이스가 있는 패키지
public class FeignConfig {

}