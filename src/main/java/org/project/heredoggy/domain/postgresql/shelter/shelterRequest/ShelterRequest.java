package org.project.heredoggy.domain.postgresql.shelter.shelterRequest;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class ShelterRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member requester;

    @Column(nullable = false)
    private String shelterName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    private String description;

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // PENDING, APPROVED, REJECTED

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}