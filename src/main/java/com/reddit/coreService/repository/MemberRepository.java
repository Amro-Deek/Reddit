package com.reddit.coreService.repository;

import com.reddit.coreService.model.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface MemberRepository extends JpaRepository<CommunityMember,Long> {
    Optional<CommunityMember> findByUserIdAndCommunityIdAndDeletedFalse(long userId, long communityId);
}
