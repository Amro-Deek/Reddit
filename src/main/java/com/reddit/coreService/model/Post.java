package com.reddit.coreService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = true)
    private Community community;

    @NotBlank
    @Column(length = 255)
    private String title;

    @Lob // large content
    private String content;

    private String imageUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt= LocalDateTime.now();

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private int commentsCount = 0;

    @Column(nullable = false)
    private int likesCount = 0;

    private boolean deleted = false ;


}

