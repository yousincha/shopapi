package com.example.shopapi.service;

import com.example.shopapi.dto.MemberUpdateDto;
import com.sun.tools.jconsole.JConsoleContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopapi.domain.Member;
import com.example.shopapi.domain.Role;
import com.example.shopapi.repository.MemberRepository;
import com.example.shopapi.repository.RoleRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Transactional(readOnly = true)
    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다."));
    }

    @Transactional
    public Member addMember(Member member) {
        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
        member.addRole(userRole.get());
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMember(Long memberId){
        return memberRepository.findById(memberId);
    }

    @Transactional(readOnly = true)
    public Optional<Member> getMember(String email){
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public void sendPasswordResetLink(String email) {
        logger.info ("sendPasswordResetLink called with email: {}", email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        String resetToken = generateResetToken(member);
        // 엔티티에 resetToken 저장
        member.setResetToken(resetToken);
        member.setResetTokenCreationTime(LocalDateTime.now());
        // 데이터베이스에 저장
        memberRepository.save(member);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setFrom(username);
        message.setSubject("비밀번호 재설정 링크");
        message.setText("비밀번호를 재설정하려면 다음 링크를 클릭하세요: " +
                "http://localhost:3000/resetpassword?token=" + resetToken);

        try {
            logger.info ("Attempting to send email to: {}", member.getEmail ());
            mailSender.send(message);
            logger.info ("Password reset link sent successfully to: {}", member.getEmail ());
        } catch (MailException e) {
            logger.error ("Failed to send email: {}", e.getMessage (), e);
            throw new RuntimeException("이메일 전송 중 오류가 발생했습니다.");
        }
    }

    private String generateResetToken(Member member) {
        return UUID.randomUUID().toString();
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByResetToken(String resetToken) {
        return memberRepository.findByResetToken(resetToken);
    }


    @Transactional
    public Member updateMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateMember(Long memberId, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        member.setName(memberUpdateDto.getName());
        member.setEmail(memberUpdateDto.getEmail());
        member.setBirthYear(Integer.parseInt(memberUpdateDto.getBirthYear()));
        member.setBirthMonth(Integer.parseInt(memberUpdateDto.getBirthMonth()));
        member.setBirthDay(Integer.parseInt(memberUpdateDto.getBirthDay()));
        member.setGender(memberUpdateDto.getGender());
        return memberRepository.save(member);
    }

}