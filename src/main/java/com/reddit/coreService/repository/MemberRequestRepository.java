package com.reddit.coreService.repository;

import com.reddit.coreService.model.MemberRequest;
import com.reddit.coreService.model.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRequestRepository extends JpaRepository<MemberRequest, Long> {

    Optional<MemberRequest> findByUserIdAndCommunityIdAndStatus(long userId, long communityId, RequestStatus requestStatus);

    // countQuery defines a special count query that shall be used for pagination
    // queries to look up the total number of elements for a page.
    @Query(
            value = """
        SELECT r.* 
        FROM member_request r
        INNER JOIN community c ON r.community_id = c.id
        WHERE r.community_id = :communityId AND r.status = 'PENDING'
        """,
            countQuery = """
        SELECT count(*) 
        FROM member_request r
        INNER JOIN community c ON r.community_id = c.id
        WHERE r.community_id = :communityId AND r.status = 'PENDING'
        """,
            nativeQuery = true
    )Page<MemberRequest> findPendingRequestsByCommunityId(@Param("communityId") Long communityId, Pageable pageable);

    @Query("""
            SELECT mr FROM MemberRequest mr 
            LEFT JOIN FETCH mr.assignedBy 
            LEFT JOIN FETCH mr.community 
            WHERE mr.id = :id AND mr.canceled = false
            """ )
    Optional<MemberRequest> findWithAssignedBy(@Param("id") Long id);
}
