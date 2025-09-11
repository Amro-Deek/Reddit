package com.reddit.coreService.repository;

import com.reddit.coreService.model.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CommunityMemberRepository extends JpaRepository<CommunityMember,Long> {
    boolean existsByUserIdAndCommunityIdAndDeletedFalse(long userId, long communityId);
}
