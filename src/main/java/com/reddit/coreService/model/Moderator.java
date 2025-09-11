package com.reddit.coreService.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "moderator")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Moderator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private Long userId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne(optional = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")  // F.key to the Moderator who assigned this user
    private Moderator assignedBy;

    @OneToMany(mappedBy = "moderator")
    private List<ModeratorPrivilege> privilegeList;

    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime deletedAt;

    private boolean deleted = false ;

}
