package com.ssginc.nojam.system.mapper;

import com.ssginc.nojam.system.vo.MapVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapMapper {
    List<MapVO> getAllBranches();  // 모든 branch 데이터를 가져오는 메서드
}

