package com.ssginc.nojam.order.controller;

import com.ssginc.nojam.order.mapper.OrderMapperQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


/**
 * @author Queue-ri
 */

@Slf4j
@Controller
@RequestMapping("order")
@RequiredArgsConstructor
public class OrderControllerQ {
    private final OrderMapperQ orderMapperQ;

    @Value("${ocr.secret}")
    private String ocrSecret;

    @GetMapping("/upload")
    public String upload(Model model) {
        model.addAttribute("ocrSecret", ocrSecret);
        return "order/upload";
    }
}
