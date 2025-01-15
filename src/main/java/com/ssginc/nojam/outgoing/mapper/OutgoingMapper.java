package com.ssginc.nojam.outgoing.mapper;

import com.ssginc.nojam.outgoing.dto.OutgoingViewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Queue-ri
 */

@Mapper
public interface OutgoingMapper {
    // 조회 메소드
    int countAllOutgoings();
    List<OutgoingViewDTO> get50Outgoings(int startIdx);

    int countAllPendingOutgoings();
    List<OutgoingViewDTO> get50PendingOutgoings(int startIdx);
    List<OutgoingViewDTO> get50IndeliveryOutgoings(int startIdx);
    void confirm(@Param("orderId") Long orderId, @Param("outStartTime") LocalDateTime outStartTime);
    void reject(Long orderId);

    // confirm시 출고내역 신규 등록해야 함
    int insertNewOutgoing(@Param("orderId") Long orderId, @Param("quantity") Integer quantity, @Param("userId") String userId, @Param("outStartTime") LocalDateTime outStartTime);

    // confirm시 본사에서 발주요청된 수량만큼 재고 차감
    void decHeadStock(@Param("decValue") Integer decValue, @Param("itemId") Long itemId);

    // 출고 완료 처리
    int countAllIndeliveryOutgoings();
    void markDone(
            @Param("orderId") Long orderId,
            @Param("incomingId") String incomingId,
            @Param("itemId") Long itemId,
            @Param("branchId") String branchId,
            @Param("quantity") Integer quantity,
            @Param("markTime") LocalDateTime markTime
    );

    // markDone시 출고완료(=발주 기준으로 '입고완료')에 따른 해당 지점의 재고 추가
    void incBranchStock(@Param("incValue") Integer incValue, @Param("itemId") Long itemId);
}
