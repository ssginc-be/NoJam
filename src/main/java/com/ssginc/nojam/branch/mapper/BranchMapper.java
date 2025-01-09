package com.ssginc.nojam.branch.mapper;

import com.ssginc.nojam.branch.vo.BranchVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Queue-ri
 */

@Mapper
public interface BranchMapper {

    int insertBranchList(List<BranchVO> branchList);
    int countBranchId(String branchId);
}
