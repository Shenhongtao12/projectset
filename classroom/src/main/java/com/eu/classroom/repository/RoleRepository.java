package com.eu.classroom.repository;

import com.eu.classroom.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Aaron
 * @date 2021/3/12 23:02
 */
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    Boolean existsRoleByNode(String node);

    /**
     * 123
     * @param node
     * @param id
     * @return
     */
    @Query(value = "select id from Role where node = ?1 and id != ?2")
    Integer checkNode(String node, Integer id);
}
