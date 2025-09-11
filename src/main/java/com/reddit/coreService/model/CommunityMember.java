package com.reddit.coreService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "community_member")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommunityMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    private Long userId ;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt= LocalDateTime.now();

    private LocalDateTime deletedAt;

    private boolean deleted = false ;

    @ManyToOne(optional = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private Moderator assignedBy;

    @PrePersist
    public void incrementCommunityMembers() {
        if (community != null) {
            community.setMembersCount(community.getMembersCount() + 1);
        }
    }
    @PreRemove
    public void decrementCommunityMembers() {
        if (community != null && community.getMembersCount() > 0) {
            community.setMembersCount(community.getMembersCount() - 1);
            deletedAt =LocalDateTime.now();
            deleted =true;
        }
    }


}
