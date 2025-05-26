package org.project.heredoggy.domain.postgresql.member;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.shelter.shelter.Shelter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    private LocalDate birth;

    @Column(nullable = false)
    private String phone;

    private String address;

    private String profileImageUrl;

    private Boolean isNotificationEnabled = true;

    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    private Double totalWalkDistance = 0.0;

    private Long totalWalkDuration = 0L;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "shelterAdmin")
    private Shelter shelter;
}