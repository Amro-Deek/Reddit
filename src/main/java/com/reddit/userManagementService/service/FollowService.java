package com.reddit.userManagementService.service;

import com.reddit.userManagementService.mapper.FollowMapper;
import com.reddit.userManagementService.mapper.UserMapper;
import com.reddit.userManagementService.model.Follower;
import com.reddit.userManagementService.model.User;
import com.reddit.userManagementService.repository.FollowRepository;
import com.reddit.userManagementService.repository.UserRepository;
import com.reddit.userManagementService.service.dto.response.FollowDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowService {
    private final FollowRepository followerRepository ;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FollowMapper followMapper;

@Transactional
    public FollowDTO follow(Long followerId, Long followedId) {
        if (followerId.equals(followedId)){
            throw new IllegalArgumentException("You can not follow yourself !");
        }
        User follower = userRepository.findByIdAndActiveTrue(followerId)
                .orElseThrow(()-> new EntityNotFoundException("Follower not found"));

        User followed = userRepository.findByIdAndActiveTrue(followedId)
                .orElseThrow(() -> new EntityNotFoundException("Followed user not found"));

        boolean alreadyFollowing = followerRepository.existsByFollowerAndFollowedAndActiveTrue(follower,followed);
        if (alreadyFollowing){
            throw new RuntimeException("Already following this user.");
        }
        Follower relation = new Follower();
        relation.setFollower(follower);
        relation.setFollowed(followed);
        relation.setActive(true);
        followerRepository.save(relation);

        FollowDTO followDTO = followMapper.fromUser(followed);

        return followDTO;//returns followed user dto object .

    }

    public FollowDTO unfollow(Long followerId, Long followedId) {
        if (followerId.equals(followedId)){
            throw new IllegalArgumentException("You can not unfollow yourself !");
        }
        User follower = userRepository.findByIdAndActiveTrue(followerId)
                .orElseThrow(()-> new EntityNotFoundException("Follower not found"));

        User followed = userRepository.findByIdAndActiveTrue(followedId)
                .orElseThrow(() -> new EntityNotFoundException("Followed user not found"));


        Follower followerRecord = followerRepository
                .findByFollowerIdAndFollowedIdAndActiveTrue(followerId, followedId).orElseThrow(() -> new RuntimeException("Already unfollowed this user !"));
        FollowDTO followDTO = followMapper.fromUser(followed);
        followerRecord.setActive(false);
        followerRepository.save(followerRecord);
        return followDTO;

    }


    public Page<FollowDTO> getFollowers(Long id, Pageable pageable) {
        User followedUser = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Page<Follower> page = followerRepository.findByFollowedAndActiveTrue(followedUser, pageable);

        return page
                .map(f -> followMapper.fromUser(f.getFollower()));
    }

    public Page<FollowDTO> getFollowing(Long id, Pageable pageable) {
        User followerUser = userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Page<Follower> page = followerRepository.findByFollowerAndActiveTrue(followerUser, pageable);

        return page
                .map(f -> followMapper.fromUser(f.getFollowed()));
    }
}
