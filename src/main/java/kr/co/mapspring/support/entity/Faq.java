package kr.co.mapspring.support.entity;

import jakarta.persistence.*;
import kr.co.mapspring.user.entity.User;
import lombok.*;

@Entity
@Table(name = "faq")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id", nullable = false, updatable = false)
    private Long faqId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "question", nullable = false, length = 100)
    private String question;

    @Column(name = "faq_answer", nullable = false, length = 500)
    private String answer;

    public void update(String question, String answer) {
    	this.question = question;
    	this.answer = answer;
    }
    
    @Builder
    private Faq(User user, String question, String answer) {
    	this.user = user;
        this.question = question;
        this.answer = answer;
    }
    
    
}