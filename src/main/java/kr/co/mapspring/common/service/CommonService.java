package kr.co.mapspring.common.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {

	
	private final EmailService emailService;
 
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
 
        emailService.sendEmail(to, subject, content);
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
 
        emailService.sendEmail(to, subject, content);
    }
}
