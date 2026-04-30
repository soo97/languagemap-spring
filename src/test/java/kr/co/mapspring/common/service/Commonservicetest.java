package kr.co.mapspring.common.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.co.mapspring.global.exception.common.EmailSendException;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommonService 테스트")
public class Commonservicetest {

    @Mock
    private EmailService emailService; // JavaMailSender 대신 EmailService를 Mock으로 변경

    @InjectMocks
    private CommonService commonservice; // 이제 가짜 emailService가 여기에 주입됩니다

    // ── sendCounselAnswerNotification 테스트 ────────────────────────────

    @Test
    @DisplayName("문의 답변 알림 이메일 발송 성공")
    void sendCounselAnswerNotification_성공() {
        // when
        commonservice.sendCounselAnswerNotification("test@test.com", "로그인 후 홈 이동 여부");

        // then - 이제 mailSender가 아니라 emailService의 메서드가 호출됐는지 확인합니다
        verify(emailService, times(1)).sendEmail(eq("test@test.com"), anyString(), anyString());
    }

    @Test
    @DisplayName("문의 답변 알림 - 이메일 주소 null 시 예외 발생")
    void sendCounselAnswerNotification_이메일_null_예외() {
        // given - emailService.sendEmail에 null이 들어오면 예외를 던지도록 설정
        doThrow(new EmailSendException()).when(emailService).sendEmail(eq(null), anyString(), anyString());

        // when & then
        assertThrows(EmailSendException.class,
                () -> commonservice.sendCounselAnswerNotification(null, "로그인 후 홈 이동 여부"));
    }

    // ── sendNoticeNotification 테스트 ────────────────────────────

    @Test
    @DisplayName("공지사항 알림 이메일 발송 성공")
    void sendNoticeNotification_성공() {
        // when
        commonservice.sendNoticeNotification("test@test.com", "4월 업데이트 안내");

        // then
        verify(emailService, times(1)).sendEmail(eq("test@test.com"), anyString(), anyString());
    }

    @Test
    @DisplayName("공지사항 알림 - 이메일 주소 null 시 예외 발생")
    void sendNoticeNotification_이메일_null_예외() {
        // given
        doThrow(new EmailSendException()).when(emailService).sendEmail(eq(null), anyString(), anyString());

        // when & then
        assertThrows(EmailSendException.class,
                () -> commonservice.sendNoticeNotification(null, "4월 업데이트 안내"));
    }
}
