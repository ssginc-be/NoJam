package com.ssginc.nojam.stock.mapper;

import com.ssginc.nojam.crawl.vo.ItemVO;
import com.ssginc.nojam.stock.vo.BranchStockVO;
import com.ssginc.nojam.stock.vo.HeadStockVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Queue-ri
 */

@Mapper
public interface StockMapper {

    List<ItemVO> getAllItem();
    int insertHeadStockList(List<HeadStockVO> headStockList);
    int insertBranchStockList(List<BranchStockVO> branchStockList);
}
