package com.reddit.coreService.repository;

import com.reddit.coreService.model.Community;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CommunityRepository extends JpaRepository<Community,Long> {

    @Query("SELECT c FROM Community c WHERE c.name = :name AND c.deleted = false")
    Optional<Community> findByName(@Param("name") String name);
    @Query("SELECT  c from Community c where c.id=:id AND c.deleted = false")
    Optional<Community> findByIdAndDeletedFalse(@Param("id")long communityId);


}
