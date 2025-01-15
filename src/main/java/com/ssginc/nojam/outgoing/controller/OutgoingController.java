package com.ssginc.nojam.outgoing.controller;

import com.ssginc.nojam.outgoing.dto.OutgoingViewDTO;
import com.ssginc.nojam.outgoing.mapper.OutgoingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * @author Queue-ri
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("outgoing")
public class OutgoingController {
    private final OutgoingMapper outgoingMapper;

    @GetMapping("/view")
    public String viewOutgoingDefault(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("pdx", 1);
        return "redirect:/outgoing/view/{pdx}";
    }

    @GetMapping("/view/{pdx}")
    public String viewOutgoing(@PathVariable("pdx") int pdx, Model model) {
        int pageIdx = (pdx-1) * 50;

        int totalRow = outgoingMapper.countAllOutgoings(); // 총 totalRow개의 검색 결과
        int pageBlockSize = 5; // 1 2 3 4 5
        int pageNavSize = (int)Math.ceil((double)totalRow / pageBlockSize); // << >> 를 몇 번 할 수 있는지

        List<OutgoingViewDTO> ogList = outgoingMapper.get50Outgoings(pageIdx);

        model.addAttribute("ogList", ogList);
        model.addAttribute("totalRow", totalRow);
        model.addAttribute("pageBlockSize", pageBlockSize);
        model.addAttribute("pageNavSize", pageNavSize);

        return "outgoing/view";
    }

    @GetMapping("/pending")
    public String viewPendingOutgoingDefault(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("pdx", 1);
        return "redirect:/outgoing/pending/{pdx}";
    }

    @GetMapping("/pending/{pdx}")
    public String viewPendingOutgoing(@PathVariable("pdx") int pdx, Model model) {
        int pageIdx = (pdx-1) * 50;

        int totalRow = outgoingMapper.countAllPendingOutgoings(); // 총 totalRow개의 검색 결과
        int pageBlockSize = 5; // 1 2 3 4 5
        int pageNavSize = (int)Math.ceil((double)totalRow / pageBlockSize); // << >> 를 몇 번 할 수 있는지

        List<OutgoingViewDTO> poList = outgoingMapper.get50PendingOutgoings(pageIdx);

        model.addAttribute("poList", poList);
        model.addAttribute("totalRow", totalRow);
        model.addAttribute("pageBlockSize", pageBlockSize);
        model.addAttribute("pageNavSize", pageNavSize);

        return "outgoing/pending";
    }

    @GetMapping("/indelivery")
    public String viewIndeliveryOutgoingDefault(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("pdx", 1);
        return "redirect:/outgoing/indelivery/{pdx}";
    }

    @GetMapping("/indelivery/{pdx}")
    public String viewIndeliveryOutgoing(@PathVariable("pdx") int pdx, Model model) {
        int pageIdx = (pdx-1) * 50;

        int totalRow = outgoingMapper.countAllIndeliveryOutgoings(); // 총 totalRow개의 검색 결과
        int pageBlockSize = 5; // 1 2 3 4 5
        int pageNavSize = (int)Math.ceil((double)totalRow / pageBlockSize); // << >> 를 몇 번 할 수 있는지

        List<OutgoingViewDTO> idoList = outgoingMapper.get50IndeliveryOutgoings(pageIdx);

        model.addAttribute("idoList", idoList);
        model.addAttribute("totalRow", totalRow);
        model.addAttribute("pageBlockSize", pageBlockSize);
        model.addAttribute("pageNavSize", pageNavSize);

        return "outgoing/indelivery";
    }

}
