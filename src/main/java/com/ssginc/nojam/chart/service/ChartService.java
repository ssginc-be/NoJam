package com.ssginc.nojam.chart.service;

import com.ssginc.nojam.chart.mapper.ChartMapper;
import com.ssginc.nojam.chart.vo.GetBranchIdAndNameDto;
import com.ssginc.nojam.chart.vo.GetSalesAndDateDto;
import com.ssginc.nojam.chart.vo.GetStatusAndCountDto;
import com.ssginc.nojam.chart.vo.GetStatusAndPercentageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<GetSalesAndDateDto> getSalesByDayOfWeek(String endDate, String branchId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(endDate, formatter);

        LocalDate newDate = date.minusDays(6);

        String startDate = newDate.format(formatter);

        System.out.println("Service Start Date = " + startDate);
        System.out.println("Service End Date = " + endDate);

        return chartMapper.getSalesByDayOfWeek(startDate, endDate, branchId);
    }

    public int getSalesAmountByDate(String branchId, String date) {
        return chartMapper.getSalesAmountByDate(branchId, date);
    }

    public int getSalesAmountByMonth(String branchId, String date) {
        return chartMapper.getSalesAmountByMonth(branchId, date);
    }

    public int getSalesAmountByYear(String branchId, String date) {
        return chartMapper.getSalesAmountByYear(branchId, date);
    }

    public List<GetStatusAndPercentageDto> getStatusCount() {

        List<GetStatusAndCountDto> statusCounts = chartMapper.getStatusCount();

        int total = statusCounts.stream()
                .mapToInt(GetStatusAndCountDto::getCount)
                .sum();

        return statusCounts.stream()
                .map(dto -> {
                    double percentage = (double) dto.getCount() / total * 100;

                    return new GetStatusAndPercentageDto(dto.getStatus(), percentage);
                })
                .collect(Collectors.toList());
    }

}
