package kr.co.mapspring.common.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.mail.internet.MimeMessage;
import kr.co.mapspring.common.dto.NotificationSettingDto;
import kr.co.mapspring.user.entity.User;
import kr.co.mapspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {

	private final JavaMailSender mailSender;
    private final UserRepository userRepository;
 
    // ══════════════════════════════════════════════
    // 알림 설정
    // ══════════════════════════════════════════════
 
    // 알림 설정 조회
    @Transactional(readOnly = true)
    public NotificationSettingDto.Response getNotificationSetting(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없어요."));
 
        return NotificationSettingDto.Response.from(user.isEmailNotification());
    }
 
    // 알림 설정 변경
    @Transactional
    public NotificationSettingDto.Response updateNotificationSetting(
            Long userId, NotificationSettingDto.Request request) {
 
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없어요."));
 
        user.updateEmailNotification(request.isEmailNotification());
 
        return NotificationSettingDto.Response.from(user.isEmailNotification());
    }
 
    // ══════════════════════════════════════════════
    // 이메일 발송
    // ══════════════════════════════════════════════
 
    // 이메일 발송 공통 메서드 (비동기)
    @Async
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
 
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML 형식
 
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 실패: " + e.getMessage());
        }
    }
 
    // 문의 답변 등록 알림
    public void sendCounselAnswerNotification(Long userId, String counselName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없어요."));
 
        // 알림 수신 여부 확인
        if (!user.isEmailNotification()) return;
 
        String subject = "[Mapingo] 문의하신 내용에 답변이 등록되었어요.";
        String content = """
                <div style="font-family: sans-serif; max-width: 600px; margin: 0 auto;">
                    <h2 style="color: #1d9e75;">Mapingo 고객지원</h2>
                    <p>안녕하세요! Mapingo입니다.</p>
                    <p>문의하신 <strong>"%s"</strong>에 답변이 등록되었어요.</p>
                    <p>Mapingo 고객지원 페이지에서 확인해보세요.</p>
                    <br>
                    <a href="https://mapingo.com/support/inquiry"
                       style="background-color: #1d9e75; color: white; padding: 10px 20px;
                              text-decoration: none; border-radius: 6px;">
                        답변 확인하기
                    </a>
                    <br><br>
                    <p style="color: #888; font-size: 12px;">본 메일은 발신 전용입니다.</p>
                </div>
                """.formatted(counselName);
 
        sendEmail(user.getEmail(), subject, content);
    }
 
    // 공지사항 등록 알림
    public void sendNoticeNotification(Long userId, String noticeTitle) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없어요."));
 
        // 알림 수신 여부 확인
        if (!user.isEmailNotification()) return;
 
        String subject = "[Mapingo] 새로운 공지사항이 등록되었어요.";
        String content = """
                <div style="font-family: sans-serif; max-width: 600px; margin: 0 auto;">
                    <h2 style="color: #1d9e75;">Mapingo 공지사항</h2>
                    <p>안녕하세요! Mapingo입니다.</p>
                    <p>새로운 공지사항 <strong>"%s"</strong>이 등록되었어요.</p>
                    <br>
                    <a href="https://mapingo.com/support/notices"
                       style="background-color: #1d9e75; color: white; padding: 10px 20px;
                              text-decoration: none; border-radius: 6px;">
                        공지사항 확인하기
                    </a>
                    <br><br>
                    <p style="color: #888; font-size: 12px;">본 메일은 발신 전용입니다.</p>
                </div>
                """.formatted(noticeTitle);
 
        sendEmail(user.getEmail(), subject, content);
    }
}
