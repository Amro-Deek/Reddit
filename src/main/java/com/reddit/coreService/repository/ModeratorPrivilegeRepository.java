package com.reddit.coreService.repository;

import com.reddit.coreService.model.Moderator;
import com.reddit.coreService.model.ModeratorPrivilege;
import com.reddit.coreService.model.Privilege;
import com.reddit.coreService.model.PrivilegeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ModeratorPrivilegeRepository extends JpaRepository<ModeratorPrivilege,Long> {
    @Query("SELECT mp.privilege.name FROM ModeratorPrivilege mp " +
            "WHERE mp.moderator = :moderator " +
            "AND mp.deleted = false " +
            "AND mp.privilege.deleted = false")
    List<PrivilegeName> findActivePrivilegesByModerator
            (@Param("moderator") Moderator moderator);

    List<ModeratorPrivilege> findByModeratorIn(List<Moderator> moderators);
}
