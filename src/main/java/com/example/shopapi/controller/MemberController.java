package com.example.shopapi.controller;

import com.example.shopapi.domain.Member;
import com.example.shopapi.domain.RefreshToken;
import com.example.shopapi.domain.Role;
import com.example.shopapi.dto.*;
import com.example.shopapi.security.jwt.util.IfLogin;
import com.example.shopapi.security.jwt.util.JwtTokenizer;
import com.example.shopapi.security.jwt.util.LoginUserDto;
import com.example.shopapi.service.MemberService;
import com.example.shopapi.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid MemberSignupDto memberSignupDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Member member = new Member();
        member.setName(memberSignupDto.getName());
        member.setEmail(memberSignupDto.getEmail());
        member.setPassword(passwordEncoder.encode(memberSignupDto.getPassword()));
        member.setBirthYear(Integer.parseInt(memberSignupDto.getBirthYear()));
        member.setBirthMonth(Integer.parseInt(memberSignupDto.getBirthMonth()));
        member.setBirthDay(Integer.parseInt(memberSignupDto.getBirthDay()));
        member.setGender(memberSignupDto.getGender());

        Member saveMember = memberService.addMember(member);

        MemberSignupResponseDto memberSignupResponse = new MemberSignupResponseDto();
        memberSignupResponse.setMemberId(saveMember.getMemberId());
        memberSignupResponse.setName(saveMember.getName());
        memberSignupResponse.setRegdate(saveMember.getRegdate());
        memberSignupResponse.setEmail(saveMember.getEmail());

        // 회원가입
        return new ResponseEntity(memberSignupResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid MemberLoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        // email이 없을 경우 Exception이 발생한다. Global Exception에 대한 처리가 필요하다.
        Member member = memberService.findByEmail(loginDto.getEmail());
        if(!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        // List<Role> ===> List<String>
        List<String> roles = member.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        // JWT토큰을 생성하였다. jwt라이브러리를 이용하여 생성.
        String accessToken = jwtTokenizer.createAccessToken(member.getMemberId(), member.getEmail(), member.getName(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(member.getMemberId(), member.getEmail(), member.getName(), roles);

        // RefreshToken을 DB에 저장한다. 성능 때문에 DB가 아니라 Redis에 저장하는 것이 좋다.
        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenEntity.setMemberId(member.getMemberId());
        refreshTokenService.addRefreshToken(refreshTokenEntity);

        MemberLoginResponseDto loginResponse = MemberLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .memberId(member.getMemberId())
                .nickname(member.getName())
                .build();
        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity logout(@RequestBody RefreshTokenDto refreshTokenDto) {
        refreshTokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken());
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity requestRefresh(@RequestBody RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenService.findRefreshToken(refreshTokenDto.getRefreshToken()).orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken.getValue());

        Long memberId = Long.valueOf((Integer)claims.get("memberId"));

        Member member = memberService.getMember(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));


        List roles = (List) claims.get("roles");
        String email = claims.getSubject();

        String accessToken = jwtTokenizer.createAccessToken(memberId, email, member.getName(), roles);

        MemberLoginResponseDto loginResponse = MemberLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenDto.getRefreshToken())
                .memberId(member.getMemberId())
                .nickname(member.getName())
                .build();
        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity userinfo(@IfLogin LoginUserDto loginUserDto) {
        Member member = memberService.findByEmail(loginUserDto.getEmail());
        return new ResponseEntity(member, HttpStatus.OK);
    }
    @PostMapping("/findpassword")
    public ResponseEntity<String> findPassword(@RequestBody @Valid PasswordResetRequestDto passwordResetRequestDto, BindingResult bindingResult) {
        logger.info("Handling findpassword request for email: " + passwordResetRequestDto.getEmail());

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: " + bindingResult.getAllErrors());
            return new ResponseEntity<>("Validation errors: " + bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        try {
            memberService.sendPasswordResetLink(passwordResetRequestDto.getEmail());
            logger.info("Password reset link sent successfully to: {}", passwordResetRequestDto.getEmail());

            return ResponseEntity.ok("이메일을 전송했습니다. 이메일을 확인하세요."); // 클라이언트에게 반환할 메시지
        } catch (IllegalArgumentException e) {
            logger.error("User not found: " + e.getMessage(), e);
            return new ResponseEntity<>("User not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred: " + e.getMessage(), e);
            return new ResponseEntity<>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetDto passwordResetDto) {
        try {
            System.out.println("Received reset password request for resetToken: " + passwordResetDto.getResetToken());
            System.out.println("New password: " + passwordResetDto.getNewPassword());
            // 비밀번호 리셋 토큰을 사용하여 회원을 찾음
            Member member = memberService.findByResetToken(passwordResetDto.getResetToken())
                    .orElseThrow(() -> new IllegalArgumentException("유저 또는 토큰이 만료 되었습니다."));

            // 새 비밀번호를 암호화하여 저장
            String encryptedPassword = passwordEncoder.encode(passwordResetDto.getNewPassword());
            member.setPassword(encryptedPassword);

            // 리셋 토큰 관련 필드 초기화
            member.setResetToken(null);
            member.setResetTokenCreationTime(null);

            // 회원 정보 업데이트
            memberService.updateMember(member);

            return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("유저 또는 토큰이 만료 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("비밀번호 재설정 중 오류 발생: " + e.getMessage());
        }
    }


    @GetMapping("/resettoken")
    public ResponseEntity<String> checkResetTokenValidity(@RequestParam String resetToken) {
        if (isValidResetToken(resetToken)) {
            return ResponseEntity.ok("Reset token is valid");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Reset token is invalid or expired");
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updateMember(@PathVariable Long id, @RequestBody @Valid MemberUpdateDto memberUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Member updatedMember = memberService.updateMember(id, memberUpdateDto);
            return new ResponseEntity<>(updatedMember, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private boolean isValidResetToken(String resetToken) {
        Optional<Member> memberOptional = memberService.findByResetToken(resetToken);
        // 실제 구현에서는 토큰의 유효성을 데이터베이스에서 확인해야 합니다.
        return memberOptional.isPresent();
    }
}
