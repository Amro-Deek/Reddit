package com.reddit.coreService.repository;

import com.reddit.coreService.model.Community;
import com.reddit.coreService.model.Privilege;
import com.reddit.coreService.model.PrivilegeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {
    @Query("SELECT p from Privilege p where p.name=:name")
    Privilege findByName(@Param("name") PrivilegeName name);

    @Query("SELECT p FROM Privilege p WHERE p.deleted = false")
    List<Privilege> findAllDeletedFalse();

}
