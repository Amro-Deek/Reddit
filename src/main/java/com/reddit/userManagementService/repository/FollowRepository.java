package com.reddit.userManagementService.repository;

import com.reddit.userManagementService.model.Follower;
import com.reddit.userManagementService.model.FollowerId;
import com.reddit.userManagementService.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follower, FollowerId> {



    Optional<Follower> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    List<Follower> findByFollowed(User followed); //all users who follow "followed"

    List<Follower> findByFollower(User follower); // all users that this user is following

}
