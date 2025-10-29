package com.example.webflowjsp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
public class MvcConfig {

    @Controller
    static class HomeController {

        @GetMapping("/")
        public String index() {
            return "index";
        }
    }
}
