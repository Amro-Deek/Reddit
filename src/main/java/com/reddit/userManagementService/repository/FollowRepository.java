package com.reddit.userManagementService.repository;

import com.reddit.userManagementService.model.Follower;
import com.reddit.userManagementService.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follower,Long> {


    boolean existsByFollowerAndFollowedAndActiveTrue(User follower , User followed);

    Optional<Follower> findByFollowerIdAndFollowedIdAndActiveTrue(Long followerId, Long followedId);

    // followers of this user (who is being followed)
    @EntityGraph(attributePaths = "follower") // fetch the follower (ManyToOne -> safe with paging)
    Page<Follower> findByFollowedAndActiveTrue(User followed, Pageable pageable);

    // accounts this user is following
    @EntityGraph(attributePaths = "followed") // fetch the followed user
    Page<Follower> findByFollowerAndActiveTrue(User follower, Pageable pageable);
}
