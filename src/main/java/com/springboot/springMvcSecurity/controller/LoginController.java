package com.springboot.springMvcSecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/loginPage")
    public String login(){
        return "fancy-login";
    }
    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }
}
