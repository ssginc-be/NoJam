package com.ssginc.nojam.chart.controller;

import com.ssginc.nojam.chart.service.ChartService;
import com.ssginc.nojam.chart.vo.GetBranchIdAndNameDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("chart")
public class ChartController {

    private final ChartService chartService;

    @GetMapping("/")
    public String chart(Model model, HttpSession session) {
        System.out.println("==============================================");
        System.out.println("GET request to connect home page received...");

        String userRole = (String) session.getAttribute("userRole");
        System.out.println("==============================================");
        System.out.println("userRole >>>> " + userRole);

        if (userRole.equals("HEAD")) {
            List<GetBranchIdAndNameDto> branchList = chartService.getBranches();

            if (!branchList.isEmpty()) {
                model.addAttribute("branches", branchList);
            }

            return "/chart/chart";
        } else if (userRole.equals("BMNG") || userRole.equals("BWKR")) {
            return "/chart/chart";
        } else {
            return "error/error";
        }
    }

    // 차트의 xLabel을 가져오기 위한 메서드
    @GetMapping("categoryLabel")
    @ResponseBody
    public List<String> getCategoryLabels() {
        System.out.println("==============================================");
        System.out.println("GET request to get Category Labels received...");
        System.out.println("==============================================");

        List<String> category2Labels = chartService.getCategoryLabels();

        System.out.println("==============================================");
        System.out.println(category2Labels);
        System.out.println("==============================================");

        return category2Labels;
    }

    @GetMapping("salesByCategory")
    @ResponseBody
    public List<Integer> getSalesByCategory(@RequestParam String branchId) {
        System.out.println("==============================================");
        System.out.println("GET request to get Sales By Category received...");
        System.out.println("==============================================");
        System.out.println("branch id >>>> \"" + branchId + "\"");


        List<Integer> salesByCategoryData = chartService.getSalesByCategory(branchId);

        System.out.println("==============================================");
        System.out.println(salesByCategoryData);
        System.out.println("==============================================");

        return salesByCategoryData;
    }

}
