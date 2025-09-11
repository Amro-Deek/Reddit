package com.reddit.coreService.repository;

import com.reddit.coreService.model.Moderator;
import com.reddit.coreService.model.ModeratorPrivilege;
import com.reddit.coreService.model.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator,Long> {
    @Query(" SELECT m FROM Moderator m JOIN FETCH m.community c " +
            "WHERE m.userId = :userId AND c.id = :communityId AND m.deleted = false ")
    Optional<Moderator> findByUserIdAndCommunityIdAndDeletedFalse
            (@Param("userId") Long userId,
             @Param("communityId") Long communityId);

    List<Moderator> findByAssignedByIsNull();



    @Query(value = """
            SELECT m 
            FROM Moderator m
            JOIN FETCH m.privilegeList
            
            WHERE
            m.community.id = :communityId AND m.deleted = false
            """, countQuery = """
            
            """)
    Page<Moderator> findByCommunityId(
            @Param("communityId") Long communityId,
            Pageable pageable
    );

    @Query("SELECT mp FROM ModeratorPrivilege mp " +
            "JOIN FETCH mp.privilege p " +
            "WHERE mp.moderator.id IN :moderatorIds AND" +
            " mp.deleted = false AND p.deleted = false")
    List<ModeratorPrivilege> findPrivilegesByModeratorIds
            (@Param("moderatorIds") List<Long> moderatorIds);



    Optional<Moderator> findByIdAndDeletedFalse(long moderatorId);

    List<Moderator> findByUserIdAndDeletedFalse(Long userId);
}
