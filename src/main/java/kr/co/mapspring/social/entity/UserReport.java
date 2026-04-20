package kr.co.mapspring.social.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_report")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false, updatable = false)
    private Long reportId;

}
