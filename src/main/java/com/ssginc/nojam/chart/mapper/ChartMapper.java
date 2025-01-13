package com.ssginc.nojam.chart.mapper;

import com.ssginc.nojam.chart.vo.GetBranchIdAndNameDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChartMapper {

    List<GetBranchIdAndNameDto> getBranches();

    List<String> getCategoryLabels();

    List<Integer> getSalesByCategory(String branchId);

    List<Integer> getIncomingByCategory(String branchId);

}
