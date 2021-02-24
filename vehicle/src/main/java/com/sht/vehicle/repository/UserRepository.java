package com.sht.vehicle.repository;

import com.sht.vehicle.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Aaron
 * @date 2021/2/12 12:37
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * check username
     * @param username username
     * @return bool
     */
    Boolean existsUserByUsername(String username);

    /**
     * user
     * @param username username
     * @return user
     */
    User findUserByUsername(String username);

    /**
     * batch delete
     * @param ids ids
     */
    void deleteUserByIdIn(List<Integer> ids);
}
