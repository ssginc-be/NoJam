package com.ssginc.nojam.order.dto;

import com.ssginc.nojam.order.vo.OrderVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponseDto {
    private List<OrderVO> items;
    private int totalCount;
    private int totalPages;
}
