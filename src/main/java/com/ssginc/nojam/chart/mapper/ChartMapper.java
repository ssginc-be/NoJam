package com.ssginc.nojam.chart.mapper;

import com.ssginc.nojam.chart.vo.GetBranchIdAndNameDto;
import com.ssginc.nojam.chart.vo.GetSalesAndDateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChartMapper {

    List<GetBranchIdAndNameDto> getBranches();

    List<String> getCategoryLabels();

    List<Integer> getSalesByCategory(String branchId);

    List<Integer> getIncomingByCategory(String branchId);

    List<GetSalesAndDateDto> getSalesByDayOfWeek(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("branchId") String branchId);

}
