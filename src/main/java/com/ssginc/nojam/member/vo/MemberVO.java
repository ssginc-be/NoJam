package com.ssginc.nojam.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberVO {

    private String userId;          // 회원 아이디
    private String userPw;          // 비밀번호
    private String userName;        // 이름
    private String userEmail;       // 이메일 주소
    private String userRole;        // 역할 (HEAD, BMNG, BWKR, GUEST)
    private LocalDateTime lastModifiedAt; // 마지막 수정 시간
    private String branchId;        // 지점 아이디 (nullable)

    @Override
    public String toString() {
        return "MemberVO{" +
                "userId='" + userId + '\'' +
                ", userPw='" + userPw + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userRole='" + userRole + '\'' +
                ", lastModifiedAt=" + lastModifiedAt +
                ", branchId='" + branchId + '\'' +
                '}';
    }
}

