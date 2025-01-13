package com.ssginc.nojam.chart.service;

import com.ssginc.nojam.chart.mapper.ChartMapper;
import com.ssginc.nojam.chart.vo.GetBranchIdAndNameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartMapper chartMapper;

    public List<GetBranchIdAndNameDto> getBranches() {
        return chartMapper.getBranches();
    }

    public List<String> getCategoryLabels() {
        return chartMapper.getCategoryLabels();
    }

    public List<Integer> getSalesByCategory(String branchId) {
        return chartMapper.getSalesByCategory(branchId);
    }

    public List<Integer> getIncomingByCategory(String branchId) {
        return chartMapper.getIncomingByCategory(branchId);
    }

}
