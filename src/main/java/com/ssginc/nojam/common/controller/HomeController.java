package com.ssginc.nojam.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("home")
public class HomeController {

    // localhost:8080/home/
    @GetMapping("/")
    public String home() {
        System.out.println("========================================");
        System.out.println("GET request to connect home page received...");

        return "/home/home";
    }

}
