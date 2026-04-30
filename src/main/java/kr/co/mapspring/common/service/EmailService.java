package kr.co.mapspring.common.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.mapspring.global.exception.common.EmailSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
	
	private final JavaMailSender mailSender;

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
            log.error("메일 발송 중 오류 발생: {}", e.getMessage(), e); // 이 로그가 있어야 구글 설정 오류를 잡습니다.
            throw new EmailSendException();
        }
    }
}
