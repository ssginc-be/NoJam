package com.ssginc.nojam.chart.controller;

import com.ssginc.nojam.chart.service.ChartService;
import com.ssginc.nojam.chart.vo.GetBranchIdAndNameDto;
import com.ssginc.nojam.chart.vo.GetSalesAndDateDto;
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

    // 차트 화면 이동 메서드
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

    // 카테고리별 판매량을 가져오기 위한 메서드
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

    // 카테고리별 입고량을 가져오기 위한 메서드
    @GetMapping("incomingByCategory")
    @ResponseBody
    public List<Integer> getIncomingByCategory(@RequestParam String branchId) {
        System.out.println("==============================================");
        System.out.println("GET request to get Incoming Quantity By Category received...");
        System.out.println("==============================================");
        System.out.println("branch id >>>> \"" + branchId + "\"");

        List<Integer> incomingByCategoryData = chartService.getIncomingByCategory(branchId);

        System.out.println("==============================================");
        System.out.println(incomingByCategoryData);
        System.out.println("==============================================");

        return incomingByCategoryData;
    }

    // 일자별 판매량을 가져오기 위한 메서드
    @GetMapping("salesByDayOfWeek")
    @ResponseBody
    public List<GetSalesAndDateDto> getSalesByDayOfWeek(@RequestParam String endDate, @RequestParam String branchId) {
        System.out.println("==============================================");
        System.out.println("GET request to get Sales By Day of week received...");
        System.out.println("==============================================");
        System.out.println("Selected End Date >>> \"" + endDate + "\"");
        System.out.println("branch id >>>> \"" + branchId + "\"");

        List<GetSalesAndDateDto> salesByDayOfWeek = chartService.getSalesByDayOfWeek(endDate, branchId);

        System.out.println("==============================================");
        System.out.println(salesByDayOfWeek);
        System.out.println("==============================================");

        return salesByDayOfWeek;
    }

    // 하루 총 판매 금액
    @GetMapping("salesAmountByDate")
    @ResponseBody
    public int getSalesAmountByDate(@RequestParam("branchId") String branchId, @RequestParam("date") String date) {
        System.out.println("==============================================");
        System.out.println("GET request to get Sales Amount By Date received...");
        System.out.println("==============================================");
        System.out.println("Date >>> \"" + date + "\"");
        System.out.println("branch id >>>> \"" + branchId + "\"");

        int salesAmountByDate = chartService.getSalesAmountByDate(branchId, date);

        System.out.println("==============================================");
        System.out.println(salesAmountByDate);
        System.out.println("==============================================");

        return salesAmountByDate;
    }

    // 한 달 총 판매 금액
    @GetMapping("salesAmountByMonth")
    @ResponseBody
    public int getSalesAmountByMonth(@RequestParam("branchId") String branchId, @RequestParam("date") String date) {
        System.out.println("==============================================");
        System.out.println("GET request to get Sales Amount By Month received...");
        System.out.println("==============================================");
        System.out.println("Date >>> \"" + date + "\"");
        System.out.println("branch id >>>> \"" + branchId + "\"");

        int salesAmountByMonth = chartService.getSalesAmountByMonth(branchId, date);

        System.out.println("==============================================");
        System.out.println(salesAmountByMonth);
        System.out.println("==============================================");

        return salesAmountByMonth;
    }

    // 일년 총 판매 금액
    @GetMapping("salesAmountByYear")
    @ResponseBody
    public int getSalesAmountByYear(@RequestParam("branchId") String branchId, @RequestParam("date") String date) {
        System.out.println("==============================================");
        System.out.println("GET request to get Sales Amount By Year received...");
        System.out.println("==============================================");
        System.out.println("Date >>> \"" + date + "\"");
        System.out.println("branch id >>>> \"" + branchId + "\"");

        int salesAmountByYear = chartService.getSalesAmountByYear(branchId, date);

        System.out.println("==============================================");
        System.out.println(salesAmountByYear);
        System.out.println("==============================================");

        return salesAmountByYear;
    }

}
