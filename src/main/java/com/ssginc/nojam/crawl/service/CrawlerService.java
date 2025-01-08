package com.ssginc.nojam.crawl.service;

import com.ssginc.nojam.crawl.NoBrandItemCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Queue-ri
 */

@Service
@RequiredArgsConstructor
public class CrawlerService {
    private final NoBrandItemCrawler crawler;

    public ResponseEntity<?> execCrawler() throws IOException {
        crawler.crawl();

        return ResponseEntity.ok().build();
    }
}
