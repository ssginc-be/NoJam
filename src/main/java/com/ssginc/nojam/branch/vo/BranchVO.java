package com.ssginc.nojam.branch.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Queue-ri
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchVO {
    private String branchId;
    private String branchName;
    private String branchAddress;
    private Double branchLatitude;
    private Double branchLongitude;
    private String postAddress;
    private LocalDateTime lastModifiedAt;
}
