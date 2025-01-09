package com.ssginc.nojam.member.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssginc.nojam.member.mapper.MemberMapper;
import com.ssginc.nojam.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;  // DI
    private final PasswordEncoder passwordEncoder;  // BCrypt 인코더 사용

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    public int signUpPwEncoder(MemberVO memberVO) {
        //mapper에게 주고 db처리해줘...
        String pw = passwordEncoder.encode(memberVO.getUserPw());
        memberVO.setUserPw(pw);
        int result = memberMapper.insertMember(memberVO);
        return result;
    }

    public boolean checkId(String userId) {
        MemberVO member = memberMapper.selectMemberById(userId);
        log.info("조회 결과: userId={}, 조회된 MemberVO={}", userId, member);
        return member == null; // null이면 중복되지 않은 상태
        // 가입하려고 하는 id를 가지고 검색을 해서
        // null이 아니면 이 id로 이미 가입이 되어있다라는 얘기 --> 사용할 수 없는 아이디로 처리!
        // null이면 이 id로 가입한 사람이 없다라는 얘기 --> 사용할 수 있는 아이디로 처리!
    }
}
