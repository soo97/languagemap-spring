package kr.co.mapspring.common.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;
import kr.co.mapspring.global.exception.common.EmailSendException;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailService 테스트")
public class Commonservicetest {


    @Mock
    private JavaMailSender mailSender;
 
    @Mock
    private MimeMessage mimeMessage;
 
    @InjectMocks
    private CommonService emailService;
 
    // ── sendEmail 테스트 ────────────────────────────
 
    @Test
    @DisplayName("이메일 발송 성공")
    void sendEmail_성공() throws Exception {
        // given
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
 
        // when
        emailService.sendEmail("test@test.com", "제목", "<p>내용</p>");
 
        // then - mailSender.send()가 1번 호출됐는지 확인
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
 
    @Test
    @DisplayName("이메일 발송 실패 시 EmailSendException 발생")
    void sendEmail_실패_예외발생() throws Exception {
        // given - MimeMessage 생성 후 send 시 예외 발생하도록 설정
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new EmailSendException()).when(mailSender).send(any(MimeMessage.class));
 
        // when & then
        assertThrows(EmailSendException.class,
                () -> emailService.sendEmail("test@test.com", "제목", "<p>내용</p>"));
    }
 
    // ── sendCounselAnswerNotification 테스트 ────────────────────────────
 
    @Test
    @DisplayName("문의 답변 알림 이메일 발송 성공")
    void sendCounselAnswerNotification_성공() throws Exception {
        // given
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
 
        // when
        emailService.sendCounselAnswerNotification("test@test.com", "로그인 후 홈 이동 여부");
 
        // then - sendEmail 내부에서 mailSender.send()가 호출됐는지 확인
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
 
    @Test
    @DisplayName("문의 답변 알림 - 이메일 주소 null 시 예외 발생")
    void sendCounselAnswerNotification_이메일_null_예외() {
    	// null 체크는 sendEmail 내부에서 하므로 mock 설정 불필요
        // when & then
        assertThrows(EmailSendException.class,
                () -> emailService.sendCounselAnswerNotification(null, "로그인 후 홈 이동 여부"));
    }
 
    // ── sendNoticeNotification 테스트 ────────────────────────────
 
    @Test
    @DisplayName("공지사항 알림 이메일 발송 성공")
    void sendNoticeNotification_성공() throws Exception {
        // given
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
 
        // when
        emailService.sendNoticeNotification("test@test.com", "4월 업데이트 안내");
 
        // then
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
 
    @Test
    @DisplayName("공지사항 알림 - 이메일 주소 null 시 예외 발생")
    void sendNoticeNotification_이메일_null_예외() {
 
        // when & then
        assertThrows(EmailSendException.class,
                () -> emailService.sendNoticeNotification(null, "4월 업데이트 안내"));
    }
}
