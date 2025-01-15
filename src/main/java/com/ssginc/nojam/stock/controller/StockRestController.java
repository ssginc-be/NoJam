package com.ssginc.nojam.stock.controller;

import com.ssginc.nojam.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Queue-ri
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("stock")
public class StockRestController {
    private final StockService service;

    @GetMapping("/head")
    public ResponseEntity<?> initHeadStock() {
        return service.initHeadStock();
    }

    @GetMapping("/branch")
    public ResponseEntity<?> initBranchStock(@RequestParam("id") String branchId) {
        return service.initBranchStock(branchId);
    }
}
