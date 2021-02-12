package com.sht.kitchen.repository;

import com.sht.kitchen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Aaron
 * @date 2021/2/12 12:37
 */
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * find by open id
     * @param openid openid
     * @return user
     */
    User findUserByOpenid(String openid);
}
