package com.ssginc.nojam.stock.controller;

import com.ssginc.nojam.stock.dto.BranchStockViewDTO;
import com.ssginc.nojam.stock.dto.HeadStockViewDTO;
import com.ssginc.nojam.stock.mapper.StockMapper;
import com.ssginc.nojam.stock.service.StockService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author Queue-ri
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("stock")
public class StockController {
    private final StockMapper stockMapper;

    private final StockService stockService;

    // 본사 재고
    @GetMapping("/head/view")
    public String viewHeadStockDefault(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("pdx", 1);
        return "redirect:/stock/head/view/{pdx}";
    }

    @GetMapping("/head/view/{pdx}")
    public String viewHeadStock(@PathVariable("pdx") int pdx, Model model) {
        int pageIdx = (pdx-1) * 50;

        int totalRow = stockMapper.countAllHeadStock(); // 총 totalRow개의 검색 결과
        int pageBlockSize = 5; // 1 2 3 4 5
        int pageNavSize = (int)Math.ceil((double)totalRow / pageBlockSize); // << >> 를 몇 번 할 수 있는지

        List<HeadStockViewDTO> hsList = stockMapper.get50HeadStock(pageIdx);

        model.addAttribute("hsList", hsList);
        model.addAttribute("totalRow", totalRow);
        model.addAttribute("pageBlockSize", pageBlockSize);
        model.addAttribute("pageNavSize", pageNavSize);

        return "stock/head-view";
    }

    @GetMapping("/head/view/query")
    public String viewHeadStockQueryDefault(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("pdx", 1);
        return "redirect:/stock/head/view/{pdx}";
    }

    @GetMapping("/head/view/query/{pdx}")
    public String viewHeadStockQuery(@PathVariable("pdx") int pdx, @RequestParam("category") String category, @RequestParam("value") String value, Model model) {
        int pageIdx = (pdx-1) * 50;

        List<HeadStockViewDTO> hsList = null;
        int totalRow = -1;
        if (category.isEmpty()) {
            hsList = stockMapper.get50HeadStock(pageIdx);
            totalRow = stockMapper.countAllHeadStock(); // 총 totalRow개의 검색 결과
        }
        else {
            hsList = stockMapper.get50FilteredHeadStock(category, value, pageIdx);
            totalRow = stockMapper.countFilteredHeadStock(category, value); // 총 totalRow개의 검색 결과
        }
        int pageBlockSize = 5; // 1 2 3 4 5
        int pageNavSize = (int)Math.ceil((double)totalRow / pageBlockSize); // << >> 를 몇 번 할 수 있는지

        model.addAttribute("hsList", hsList);

        model.addAttribute("totalRow", totalRow);
        model.addAttribute("pageBlockSize", pageBlockSize);
        model.addAttribute("pageNavSize", pageNavSize);

        model.addAttribute("category", category);
        model.addAttribute("value", value);

        return "stock/head-view-query";
    }

    
    // 지점별 재고
    @GetMapping("/branch/view")
    public String viewBranchStockDefault(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("pdx", 1);
        return "redirect:/stock/branch/view/{pdx}";
    }

    @GetMapping("/branch/view/{pdx}")
    public String viewBranchStock(@PathVariable("pdx") int pdx, Model model, HttpSession session) {
        int pageIdx = (pdx-1) * 50;
        String branchId = (String) session.getAttribute("branchId");

        int totalRow = stockMapper.countAllBranchStock(branchId); // 총 totalRow개의 검색 결과
        int pageBlockSize = 5; // 1 2 3 4 5
        int pageNavSize = (int)Math.ceil((double)totalRow / pageBlockSize); // << >> 를 몇 번 할 수 있는지

        List<BranchStockViewDTO> bsList = stockMapper.get50BranchStock(branchId, pageIdx);

        model.addAttribute("bsList", bsList);
        model.addAttribute("totalRow", totalRow);
        model.addAttribute("pageBlockSize", pageBlockSize);
        model.addAttribute("pageNavSize", pageNavSize);

        return "stock/branch-view";
    }

    @GetMapping("/branch/view/query")
    public String viewBranchStockQueryDefault(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("pdx", 1);
        return "redirect:/stock/branch/view/{pdx}";
    }

    @GetMapping("/branch/view/query/{pdx}")
    public String viewBranchStockQuery(@PathVariable("pdx") int pdx, @RequestParam("category") String category, @RequestParam("value") String value, Model model, HttpSession session) {
        int pageIdx = (pdx-1) * 50;
        String branchId = (String) session.getAttribute("branchId");

        List<BranchStockViewDTO> bsList = null;
        int totalRow = -1;
        if (category.isEmpty()) {
            bsList = stockMapper.get50BranchStock(branchId, pageIdx);
            totalRow = stockMapper.countAllBranchStock(branchId); // 총 totalRow개의 검색 결과
        }
        else {
            bsList = stockMapper.get50FilteredBranchStock(branchId, category, value, pageIdx);
            totalRow = stockMapper.countFilteredBranchStock(branchId, category, value); // 총 totalRow개의 검색 결과
        }
        int pageBlockSize = 5; // 1 2 3 4 5
        int pageNavSize = (int)Math.ceil((double)totalRow / pageBlockSize); // << >> 를 몇 번 할 수 있는지

        model.addAttribute("bsList", bsList);

        model.addAttribute("totalRow", totalRow);
        model.addAttribute("pageBlockSize", pageBlockSize);
        model.addAttribute("pageNavSize", pageNavSize);

        model.addAttribute("category", category);
        model.addAttribute("value", value);

        return "stock/branch-view-query";
    }
}
