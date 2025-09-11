package com.reddit.coreService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "moderator_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeratorRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    private Long userId;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private Moderator assignedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING; // default is pending

    private LocalDateTime requestedAt = LocalDateTime.now();
    private LocalDateTime decidedAt; // when accepted or rejected

    private boolean deleted = false ;

}
