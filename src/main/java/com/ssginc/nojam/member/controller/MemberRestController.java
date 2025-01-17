package com.ssginc.nojam.member.controller;


import com.ssginc.nojam.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberRestController {

    private final MemberService memberService;



    /*
    @PostMapping("update-role")
@ResponseBody
public Map<String, Object> updateRole(@RequestParam String userId, @RequestParam String newRole, HttpSession session) {
    String userRole = (String) session.getAttribute("userRole");
    Map<String, Object> response = new HashMap<>();

    if (!"HEAD".equals(userRole)) {
        response.put("status", "unauthorized");
        response.put("message", "권한이 없습니다.");
        return response;
    }

    boolean success = memberService.updateMemberRole(userId, newRole);
    if (success) {
        response.put("status", "success");
        response.put("message", "역할이 성공적으로 변경되었습니다.");
    } else {
        response.put("status", "fail");
        response.put("message", "역할 변경에 실패했습니다.");
    }
    return response;
}
     */

}