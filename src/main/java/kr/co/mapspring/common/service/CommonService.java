package kr.co.mapspring.common.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.mapspring.global.exception.common.EmailSendException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final JavaMailSender mailSender;
    
    /**
     * 이메일 발송 공통 메서드
     * "emailTaskExecutor" - AsyncConfig에서 설정한 ThreadPoolTaskExecutor 사용
     */
    @Async("emailTaskExecutor")
    public void sendEmail(String to, String subject, String content) {
    	if (to == null || to.isBlank()) {
            throw new EmailSendException();
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
 
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true = HTML 형식
 
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendException();
        }
    }
 
    // ── 문의 답변 등록 알림 ────────────────────────────
 
    public void sendCounselAnswerNotification(String to, String counselName) {
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
 
        sendEmail(to, subject, content);
    }
 
    // ── 공지사항 등록 알림 ────────────────────────────
 
    public void sendNoticeNotification(String to, String noticeTitle) {
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
 
        sendEmail(to, subject, content);
    }
}
