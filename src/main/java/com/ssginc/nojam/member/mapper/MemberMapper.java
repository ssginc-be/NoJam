package com.ssginc.nojam.member.mapper;

import com.ssginc.nojam.member.vo.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.ui.Model;

import java.util.List;

@Mapper
public interface MemberMapper {
    // 회원가입
    int insertMember(MemberVO memberVO);

    MemberVO selectMemberById(String userId);

}
