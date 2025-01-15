package com.ssginc.nojam.crawl.mapper;

import com.ssginc.nojam.crawl.vo.ItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Queue-ri
 */

@Mapper
public interface CrawlerMapper {

    int insertItemList(List<ItemVO> itemList);
}
