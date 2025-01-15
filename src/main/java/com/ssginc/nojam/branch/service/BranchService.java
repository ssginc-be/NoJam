package com.ssginc.nojam.branch.service;

import com.opencsv.CSVReader;
import com.ssginc.nojam.branch.mapper.BranchMapper;
import com.ssginc.nojam.branch.vo.BranchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Queue-ri
 */

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchMapper mapper;

    public ResponseEntity<?> fetchCSV() throws Exception {
        final String fileName = "branch_info.csv";
        String[] line;
        CSVReader reader = new CSVReader(new FileReader(fileName));

        System.out.println("* * * BranchService.fetchCSV(): start parsing..");

        List<BranchVO> branchList = new ArrayList<>();
        reader.readNext(); // 맨 첫 줄은 column명이므로 버림
        int idx = 1;
        while ((line = reader.readNext()) != null) {
            String bid = "B" + String.format("%05d", idx++);

            List<String> branchInfo = new ArrayList<>();
            for (String data : line) {
                if (!data.isEmpty()) {
                    branchInfo.add(data);
                }
            }

            BranchVO branchVO = BranchVO.builder()
                    .branchId(bid)
                    .branchName(branchInfo.get(0))
                    .branchAddress(branchInfo.get(3))
                    .branchLatitude(Double.parseDouble(branchInfo.get(1)))
                    .branchLongitude(Double.parseDouble(branchInfo.get(2)))
                    .postAddress(branchInfo.get(4))
                    .lastModifiedAt(LocalDateTime.now())
                    .build();

            System.out.println(branchVO.toString());
            branchList.add(branchVO);
        }

        // mybatis
        mapper.insertBranchList(branchList);

        System.out.println("* * * BranchService.fetchCSV(): parsed successfully!");


        return ResponseEntity.ok().build();
    }
    /**
     * 특정 branchId에 대한 BranchVO를 반환합니다.
     *
     * @param branchId 조회할 branch의 ID
     * @return BranchVO 객체 또는 null
     */
    public BranchVO selectBranchName(String branchId) {
        return mapper.selectBranchName(branchId);
    }
}
