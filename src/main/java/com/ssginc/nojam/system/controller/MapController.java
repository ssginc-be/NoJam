package com.ssginc.nojam.system.controller;

import com.ssginc.nojam.system.service.MapService;
import com.ssginc.nojam.system.vo.MapVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("map")
public class MapController {

    private final MapService mapService;

    @GetMapping("/")
    public String map() {
        return "/system/map";
    }

    @GetMapping("branches")
    @ResponseBody
    public List<MapVO> getBranches() {
        return mapService.getAllBranches();
    }
}
