package com.ssginc.nojam.member.controller;

import jakarta.servlet.http.HttpSession;
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

    @GetMapping("login")
    public String login(HttpSession session) {
        System.out.println("========================================");
        System.out.println("GET request to login received...");

//        session.setAttribute("role", "head office");
//        session.setAttribute("role", "branch manager");
        session.setAttribute("role", "branch worker");

        System.out.println("세션값 설정 확인 >> " + session.getAttribute("role"));

        return "redirect:/home/";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        System.out.println("========================================");
        System.out.println("GET request to logout received...");

        session.removeAttribute("role");

        return "redirect:/";
    }
}
