package com.ssginc.nojam.system.service;

import com.ssginc.nojam.system.mapper.MapMapper;
import com.ssginc.nojam.system.vo.MapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {

    @Autowired
    private MapMapper mapMapper;

    public List<MapVO> getAllBranches() {
        return mapMapper.getAllBranches();
    }
}
