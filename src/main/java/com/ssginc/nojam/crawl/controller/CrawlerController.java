package com.ssginc.nojam.crawl.controller;

import com.ssginc.nojam.crawl.service.CrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Queue-ri
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("crawler")
public class CrawlerController {
    private final CrawlerService service;

    @GetMapping
    public ResponseEntity<?> execCrawler() throws IOException {
        return service.execCrawler();
    }
}
