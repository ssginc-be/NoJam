package com.ssginc.teamproject.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("member")
public class MemberController {

    // localhost:8080/member/forgot-password
    @GetMapping("forgot-password")
    public String forgotPw() {
        System.out.println("========================================");
        System.out.println("GET request to find password received...");

        return "/member/forgot-password";
    }

    // localhost:8080/member/register
    @GetMapping("register")
    public String register() {
        System.out.println("========================================");
        System.out.println("GET request to register received...");

        return "/member/register";
    }

}
