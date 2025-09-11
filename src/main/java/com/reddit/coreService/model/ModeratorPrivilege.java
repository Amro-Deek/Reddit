package com.reddit.coreService.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "moderator_privileges")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ModeratorPrivilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private Moderator moderator;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    private Privilege privilege;

    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime deletedAt;

    private boolean deleted = false ;

}
