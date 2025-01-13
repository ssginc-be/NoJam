package com.ssginc.nojam.system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MapVO {
    private String branchId;  // branch_id는 VARCHAR(50) 이므로 String 타입
    private String branchName;
    private String branchAddress;
    private double branchLatitude;
    private double branchLongitude;
    private String postAddress;
    private LocalDateTime lastModifiedAt;  // TIMESTAMP는 LocalDateTime으로 처리
}
