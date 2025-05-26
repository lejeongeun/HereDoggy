package org.project.heredoggy.domain.postgresql.shelter.shelter;

import jakarta.persistence.*;
import lombok.*;
import org.project.heredoggy.domain.postgresql.member.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Shelter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;  //담당자 이메일

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(length = 1000)
    private String description;

    @Column(unique = true ,nullable = false)
    private String shelterCode;

    @CreatedDate
    @Column(name = "created_At")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_At")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelterAdmin_id", unique = true)
    private Member shelterAdmin;
}
