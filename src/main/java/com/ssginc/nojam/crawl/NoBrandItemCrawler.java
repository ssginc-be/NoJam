package com.ssginc.nojam.crawl;

import com.ssginc.nojam.crawl.mapper.CrawlerMapper;
import com.ssginc.nojam.crawl.vo.ItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Queue-ri
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class NoBrandItemCrawler {
    private final CrawlerMapper mapper;

    private final String[][] categoryArr = {
            {"6000229700", "신선식품", "과일/채소/양곡"}, // 2
            {"6000229701", "신선식품", "축산/수산/건식품"},
            {"6000229703", "가공식품", "과자/초콜릿"}, // 7
            {"6000229704", "가공식품", "라면/통조림"},
            {"6000229706", "가공식품", "조미료/장류"},
            {"6000229705", "가공식품", "생수/음료"},
            {"6000229707", "가공식품", "커피/차"},
            {"6000229708", "가공식품", "냉동냉장/HMR"},
            {"6000229709", "가공식품", "유제품"},
            {"6000229712", "생활용품", "화장지/위생용품"}, // 6
            {"6000229714", "생활용품", "헤어/바디/구강용품"},
            {"6000229713", "생활용품", "화장품"},
            {"6000229711","생활용품", "세제"},
            {"6000229715", "생활용품", "주방용품/일회용품"},
            {"6000229716", "생활용품", "청소/욕실용품"},
            {"6000229718", "가전/인테리어", "생활/디지털가전"}, // 7
            {"6000229719", "가전/인테리어", "문구/완구"},
            {"6000229720", "가전/인테리어", "가구/홈데코"},
            {"6000229722", "가전/인테리어", "조명/공구"},
            {"6000229721", "가전/인테리어", "침구/수예"},
            {"6000229723", "가전/인테리어", "자동차용품"},
            {"6000229724", "가전/인테리어", "애완"},
            {"6000229726", "패션잡화", "잡화"}, // 1
            {"6000229727", "스포츠/캠핑용품", ""}, // 1
    };
    private final List<List<String>> categoryList = Arrays.stream(categoryArr).map(Arrays::asList).toList();

    public void crawl() throws IOException {
        for (List<String> category : categoryList) {
            String code = category.get(0);
            String subCategory1 = category.get(1);
            String subCategory2 = category.get(2);
            final String URL = "https://emart.ssg.com/specialStore/nobrand/sub.ssg?ctgId=" + code;

            Document doc = Jsoup.connect(URL).get();
            Elements titleDiv = doc.select(".mnemitem_goods_tit");
            Elements priceDiv = doc.select(".ssg_price");
            
            // DB 저장
            System.out.println("* * * NoBrandItemCrawler.crawl(): fetched items");
            System.out.println("fetched size: " + titleDiv.size());
            List<ItemVO> itemList = new ArrayList<>();
            for (int i = 0; i < titleDiv.size(); ++i) {
                ItemVO itemVO = new ItemVO(
                        null,
                        titleDiv.get(i).text(),
                        subCategory1,
                        subCategory2,
                        Integer.parseInt(priceDiv.get(i).text().replace(",", "")),
                        LocalDateTime.now()
                );
                System.out.println(itemVO.toString());
                itemList.add(itemVO);
            }
            System.out.println();
            if (titleDiv.isEmpty()) // fetched size 0인데 insert 시도하면 에러 터짐
                System.out.println("[INSERT SKIPPED]");
            else {
                mapper.insertItemList(itemList);
                System.out.println("[INSERT DONE]");
            }
            System.out.println();
        }
    }
}
