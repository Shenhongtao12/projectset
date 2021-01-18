package com.eurasia.specialty.repository;

import com.eurasia.specialty.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    /**
     * get user
     * @param nickName
     * @return
     */
    User findUserByNickName(String nickName);
}
