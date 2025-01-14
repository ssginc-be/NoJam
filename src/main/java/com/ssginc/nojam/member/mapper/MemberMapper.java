package com.ssginc.nojam.member.mapper;

import com.ssginc.nojam.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.ui.Model;

import java.util.List;

@Mapper
public interface MemberMapper {
    // 회원가입
    int insertMember(MemberVO memberVO);

    MemberVO selectMemberById(String userId);

    // 비밀번호 찾기
    int updatePasswordByEmail(@Param("userEmail") String userEmail, @Param("userPw") String userPw);

    // 이메일로 회원 조회
    MemberVO selectMemberByEmail(@Param("userEmail") String userEmail);

    // 마이페이지 회원 정보 수정
    // 사용자명 업데이트
    int updateUserName(MemberVO memberVO);
    // 비밀번호 업데이트
    int updatePassword(@Param("userId") String userId, @Param("userPw") String userPw);
}
