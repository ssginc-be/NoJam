package com.ssginc.nojam.member.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ssginc.nojam.member.mapper.MemberMapper;
import com.ssginc.nojam.member.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;  // DI
    private final PasswordEncoder passwordEncoder;  // BCrypt 인코더 사용

    private static final Logger log = LoggerFactory.getLogger(MemberService.class);

    // pw 암호화
    public int signUpPwEncoder(MemberVO memberVO) {
        //mapper에게 주고 db처리해줘...
        String pw = passwordEncoder.encode(memberVO.getUserPw());
        memberVO.setUserPw(pw);
        int result = memberMapper.insertMember(memberVO);
        return result;
    }

    // id 중복 체크
    public boolean checkId(String userId) {
        MemberVO member = memberMapper.selectMemberById(userId);
        log.info("조회 결과: userId={}, 조회된 MemberVO={}", userId, member);
        return member == null; // null이면 중복되지 않은 상태
        // 가입하려고 하는 id를 가지고 검색을 해서
        // null이 아니면 이 id로 이미 가입이 되어있다라는 얘기 --> 사용할 수 없는 아이디로 처리!
        // null이면 이 id로 가입한 사람이 없다라는 얘기 --> 사용할 수 있는 아이디로 처리!
    }

    // 로그인
    public boolean login(MemberVO memberVO) {
        // memberVO: 입력한 id,  memberVO1: DB에 저장된 id
        MemberVO memberVO1 = memberMapper.selectMemberById(memberVO.getUserId());
        if (memberVO1 != null) {
            if (passwordEncoder.matches(memberVO.getUserPw(), memberVO1.getUserPw())) {
                return true; //로그인 성공
            } else {
                return false; //로그인 실패
            }
        } else {
            return false; //로그인 실패
        }
    }

    // ID로 회원 정보 조회
    public MemberVO getMemberById(String id) {
        return memberMapper.selectMemberById(id);
    }

    // 임시 비밀번호를 생성하고 데이터베이스에 저장
    public boolean resetPassword(String userEmail, String tempPassword) {
        // 비밀번호 암호화
        String hashedTempPassword = passwordEncoder.encode(tempPassword);
        // 데이터베이스 업데이트
        int result = memberMapper.updatePasswordByEmail(userEmail, hashedTempPassword);
        return result > 0;
    }

    // 주어진 이메일로 회원 정보 조회
    public MemberVO getMemberByEmail(String userEmail) {
        // MyBatis 매퍼에 selectMemberByEmail 메서드를 추가해야 합니다.
        return memberMapper.selectMemberByEmail(userEmail);
    }

    // 회원가입 시 이메일 중복 체크
    public boolean isEmailDuplicate(String userEmail) {
        MemberVO member = memberMapper.selectMemberByEmail(userEmail);
        return member != null; // null이 아니면 중복
    }

    // 사용자명 업데이트
    public boolean updateUserName(String userId, String newUserName) {
        if (newUserName == null || newUserName.trim().isEmpty()) {
            log.warn("새 사용자명이 비어 있습니다: userId={}", userId);
            return false;
        }

        MemberVO updatedMember = new MemberVO();
        updatedMember.setUserId(userId);
        updatedMember.setUserName(newUserName.trim());

        int result = memberMapper.updateUserName(updatedMember);
        return result > 0;
    }

    // 비밀번호 업데이트
    public boolean updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword) {
        MemberVO member = memberMapper.selectMemberById(userId);
        if (member == null) {
            log.warn("회원 정보 조회 실패: userId={}", userId);
            return false;
        }

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(currentPassword, member.getUserPw())) {
            log.warn("비밀번호 검증 실패: userId={}", userId);
            return false;
        }

        // 새로운 비밀번호가 입력된 경우 검증
        if (newPassword == null || newPassword.isEmpty()) {
            log.warn("새 비밀번호가 입력되지 않음: userId={}", userId);
            return false;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            log.warn("새 비밀번호와 확인 비밀번호가 일치하지 않음: userId={}", userId);
            return false;
        }

        // 비밀번호 길이 검증 (예: 최소 4글자)
        if (newPassword.length() < 4) {
            log.warn("새 비밀번호가 너무 짧음: userId={}", userId);
            return false;
        }

        // 비밀번호 암호화
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // 비밀번호 업데이트
        int result = memberMapper.updatePassword(userId, encodedNewPassword);
        return result > 0;
    }

    // 모든 BMNG, BWKR, GUEST 역할의 회원을 조회 (지점 회원 관리)
    public List<MemberVO> getAllMembersWithRoles() {
        return memberMapper.selectAllMembersWithRoles();
    }

    // 특정 회원의 user_role을 업데이트 (지점 회원 관리)
    public boolean updateMemberRole(String userId, String newRole) {
        // 유효한 역할인지 확인
        if (!Arrays.asList("BMNG", "BWKR", "GUEST").contains(newRole)) {
            log.warn("Invalid user role: {}", newRole);
            return false;
        }
        int result = memberMapper.updateUserRole(userId, newRole);
        return result > 0;
    }
}
