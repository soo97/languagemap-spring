package kr.co.mapspring.user.terms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.co.mapspring.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	    name = "user_term_agreement",
	    uniqueConstraints = {
	        @UniqueConstraint(name = "uk_user_term_agreement_user_term", columnNames = {"user_id", "term_id"})
	    }
	)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTermsAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_term_agreement_id", nullable = false, updatable = false)
    private Long userTermAgreementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Terms term;

    @Column(name = "agreed", nullable = false)
    private boolean agreed;

    @Column(name = "agreed_at", nullable = false)
    private LocalDateTime agreedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static UserTermsAgreement create(User user, Terms term, boolean agreed) {
        UserTermsAgreement agreement = new UserTermsAgreement();
        agreement.user = user;
        agreement.term = term;
        agreement.agreed = agreed;
        agreement.agreedAt = agreed ? LocalDateTime.now() : null;
        return agreement;
    }
}