package com.reddit.coreService.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "community")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @NotBlank(message = "Community name is required")
    @Column(nullable = false , unique = true)
    private String name ;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt= LocalDateTime.now();

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private long membersCount = 0L;

    private boolean deleted = false ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunityType type = CommunityType.PUBLIC;

}
