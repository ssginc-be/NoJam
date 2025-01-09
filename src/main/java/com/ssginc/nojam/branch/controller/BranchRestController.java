package com.ssginc.nojam.branch.controller;

import com.ssginc.nojam.branch.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Queue-ri
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("branch")
public class BranchRestController {
    private final BranchService service;

    @GetMapping("/csv")
    public ResponseEntity<?> fetchCSV() throws Exception {
        return service.fetchCSV();
    }
}
