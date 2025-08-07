package com.reddit.userManagementService.repository;

import com.reddit.userManagementService.model.Follower;
import com.reddit.userManagementService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follower,Long> {


    boolean existsByFollowerAndFollowedAndActiveTrue(User follower , User followed);

    Optional<Follower> findByFollowerIdAndFollowedIdAndActiveTrue(Long followerId, Long followedId);

    List<Follower> findByFollowedAndActiveTrue(User followed); //all users who follow "followed"

    List<Follower> findByFollowerAndActiveTrue(User follower); // all users that this user is following

}
