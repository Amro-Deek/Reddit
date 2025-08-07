package com.reddit.userManagementService.service;

import com.reddit.userManagementService.mapper.FollowMapper;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.model.Follower;
import com.reddit.userManagementService.model.FollowerId;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.repository.FollowRepository;
import com.reddit.userManagementService.repository.UserRepository;
import com.reddit.userManagementService.service.dto.response.FollowDTO;
import com.reddit.userManagementService.service.dto.response.RegisterUserDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followerRepository ;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FollowMapper followMapper;


    public FollowDTO follow(Long followerId, Long followedId) {
        if (followerId.equals(followedId)){
            throw new IllegalArgumentException("You can not follow yourself !");
        }
        User follower = userRepository.findById(followerId)
                .orElseThrow(()-> new EntityNotFoundException("Follower not found"));

        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new EntityNotFoundException("Followed user not found"));

        FollowerId relationId = new FollowerId(followerId, followedId);
        boolean alreadyFollowing = followerRepository.existsById(relationId);
        if (alreadyFollowing) {
            throw new RuntimeException("Already following this user.");
        }

        // Save with composite key
        Follower relation = new Follower();
        relation.setId(relationId);
        relation.setFollower(follower);
        relation.setFollowed(followed);
        relation.setFollowedAt(LocalDateTime.now());
        followerRepository.save(relation);

        FollowDTO followDTO = followMapper.fromUser(followed);

        return followDTO;//returns followed user dto object .

    }

    public FollowDTO unfollow(Long followerId, Long followedId) {
        if (followerId.equals(followedId)){
            throw new IllegalArgumentException("You can not unfollow yourself !");
        }
        User follower = userRepository.findById(followerId)
                .orElseThrow(()-> new EntityNotFoundException("Follower not found"));

        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new EntityNotFoundException("Followed user not found"));

        FollowerId relationId = new FollowerId(followerId, followedId);

        Follower followerRecord = followerRepository
                .findById(relationId).orElseThrow(() -> new RuntimeException("Already unfollowed this user !"));
        FollowDTO followDTO = followMapper.fromUser(followed);

        followerRepository.delete(followerRecord);
        return followDTO;

    }



    public List<FollowDTO> getFollowers(Long id) {
        User followedUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        List<Follower> followers = followerRepository.findByFollowed(followedUser);

        return followers.stream()
                .map(f -> followMapper.fromUser(f.getFollower()))
                .toList();
    }

    public List<FollowDTO> getFollowing(Long id) {
        User followerUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        List<Follower> following = followerRepository.findByFollower(followerUser);

        return following.stream()
                .map(f -> followMapper.fromUser(f.getFollowed()))
                .toList();
    }
}
