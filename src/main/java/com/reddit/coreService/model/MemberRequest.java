package com.reddit.coreService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_request")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MemberRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private Moderator assignedBy;

    @Column
    private LocalDateTime decidedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private boolean canceled = false ;

    @Column
    private LocalDateTime canceledAt;

}
