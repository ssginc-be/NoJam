package com.ssginc.nojam.stock.mapper;

import com.ssginc.nojam.crawl.vo.ItemVO;
import com.ssginc.nojam.stock.dto.HeadStockViewDTO;
import com.ssginc.nojam.stock.vo.BranchStockVO;
import com.ssginc.nojam.stock.vo.HeadStockVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Queue-ri
 */

@Mapper
public interface StockMapper {

    List<ItemVO> getAllItem();
    int insertHeadStockList(List<HeadStockVO> headStockList);
    int insertBranchStockList(List<BranchStockVO> branchStockList);
    List<HeadStockViewDTO> get50HeadStock(int startIdx);
    int countAllHeadStock();
    int countFilteredHeadStock(@Param("category") String category, @Param("value") String value);
    List<HeadStockViewDTO> get50FilteredHeadStock(@Param("category") String category, @Param("value") String value, @Param("startIdx") int startIdx);
}
