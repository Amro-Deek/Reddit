package com.reddit.userManagementService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "user_followers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Follower {
    @EmbeddedId//for normalization (composition)
    private FollowerId id;

    @ManyToOne
    @MapsId("followerId") // maps to id.followerId
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @MapsId("followedId") // maps to id.followedId
    @JoinColumn(name = "followed_id")
    private User followed;

    private LocalDateTime followedAt;
}