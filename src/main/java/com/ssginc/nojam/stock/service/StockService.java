package com.ssginc.nojam.stock.service;

import com.ssginc.nojam.branch.mapper.BranchMapper;
import com.ssginc.nojam.crawl.vo.ItemVO;
import com.ssginc.nojam.stock.mapper.StockMapper;
import com.ssginc.nojam.stock.vo.BranchStockVO;
import com.ssginc.nojam.stock.vo.HeadStockVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Queue-ri
 */

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockMapper stockMapper;
    private final BranchMapper branchMapper;

    public ResponseEntity<?> initHeadStock() {
        System.out.println("* * * StockService.initHeadStock(): fetching head stock..");

        List<ItemVO> itemList = stockMapper.getAllItem();
        List<HeadStockVO> headStockList = new ArrayList<>();
        int idx = 1;
        for (ItemVO itemVO : itemList) {
            String stockId = "HS" + String.format("%05d", idx++);
            HeadStockVO headStockVO = HeadStockVO.builder()
                    .stockId(stockId)
                    .itemId(itemVO.getItemId())
                    .stock(100)
                    .lastModifiedAt(LocalDateTime.now())
                    .build();
            headStockList.add(headStockVO);
        }
        stockMapper.insertHeadStockList(headStockList);

        System.out.println("* * * StockService.initHeadStock(): fetched successfully!");
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> initBranchStock(String branchId) {
        System.out.println("* * * StockService.initBranchStock(): received branch id [" + branchId + "]");

        // branch id 유효성 검사
        if (branchMapper.countBranchId(branchId) < 1) {
            System.out.println("* * * StockService.initBranchStock(): invalid branch id");
            return ResponseEntity.badRequest().build();
        }

        System.out.println("* * * StockService.initBranchStock(): fetching branch stock..");

        List<ItemVO> itemList = stockMapper.getAllItem();
        List<BranchStockVO> branchStockList = new ArrayList<>();
        int idx = 1;
        for (ItemVO itemVO : itemList) {
            String stockId = branchId + "_" + itemVO.getItemId();
            BranchStockVO branchStockVO = BranchStockVO.builder()
                    .stockId(stockId)
                    .itemId(itemVO.getItemId())
                    .branchId(branchId)
                    .stock(100)
                    .lastModifiedAt(LocalDateTime.now())
                    .build();
            branchStockList.add(branchStockVO);
        }
        stockMapper.insertBranchStockList(branchStockList);

        System.out.println("* * * StockService.initBranchStock(): fetched successfully!");
        return ResponseEntity.ok().build();
    }
}