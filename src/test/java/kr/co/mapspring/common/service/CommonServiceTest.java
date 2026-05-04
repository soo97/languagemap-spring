package kr.co.mapspring.common.service;

import jakarta.mail.internet.MimeMessage;
import kr.co.mapspring.global.exception.common.EmailSendException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommonServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private CommonService commonService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        mimeMessage = mock(MimeMessage.class);
        given(mailSender.createMimeMessage()).willReturn(mimeMessage);
    }

    // ── sendEmail ────────────────────────────

    @Test
    @DisplayName("이메일 발송에 성공한다")
    void 이메일_발송에_성공한다() {

        String to = "test@example.com";
        String subject = "테스트 제목";
        String content = "<p>테스트 내용</p>";

        assertDoesNotThrow(() -> commonService.sendEmail(to, subject, content));

        verify(mailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("이메일 발송 실패 시 EmailSendException이 발생한다")
    void 이메일_발송_실패시_EmailSendException이_발생한다() {

        String to = "test@example.com";
        String subject = "테스트 제목";
        String content = "<p>테스트 내용</p>";

        doThrow(new MailSendException("발송 실패"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThrows(EmailSendException.class,
                () -> commonService.sendEmail(to, subject, content));

        verify(mailSender).send(any(MimeMessage.class));
    }

    // ── sendCounselAnswerNotification ────────────────────────────

    @Test
    @DisplayName("문의 답변 알림 이메일 발송에 성공한다")
    void 문의_답변_알림_이메일_발송에_성공한다() {

        String to = "test@example.com";
        String counselName = "학습 기록 문의";

        assertDoesNotThrow(() ->
                commonService.sendCounselAnswerNotification(to, counselName));

        verify(mailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("문의 답변 알림 이메일 발송 실패 시 EmailSendException이 발생한다")
    void 문의_답변_알림_이메일_발송_실패시_EmailSendException이_발생한다() {

        String to = "test@example.com";
        String counselName = "학습 기록 문의";

        doThrow(new MailSendException("발송 실패"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThrows(EmailSendException.class,
                () -> commonService.sendCounselAnswerNotification(to, counselName));

        verify(mailSender).send(any(MimeMessage.class));
    }

    // ── sendNoticeNotification ────────────────────────────

    @Test
    @DisplayName("공지사항 알림 이메일 발송에 성공한다")
    void 공지사항_알림_이메일_발송에_성공한다() {

        String to = "test@example.com";
        String noticeTitle = "8월 점검 공지";

        assertDoesNotThrow(() ->
                commonService.sendNoticeNotification(to, noticeTitle));

        verify(mailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("공지사항 알림 이메일 발송 실패 시 EmailSendException이 발생한다")
    void 공지사항_알림_이메일_발송_실패시_EmailSendException이_발생한다() {

        String to = "test@example.com";
        String noticeTitle = "8월 점검 공지";

        doThrow(new MailSendException("발송 실패"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThrows(EmailSendException.class,
                () -> commonService.sendNoticeNotification(to, noticeTitle));

        verify(mailSender).send(any(MimeMessage.class));
    }
}