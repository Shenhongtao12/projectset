package com.eurasia.specialty.repository;

import com.eurasia.specialty.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Aaron
 * @date 2021/1/15 22:48
 */
public interface AdminRepository extends JpaRepository<Admin, Integer>, JpaSpecificationExecutor<Admin> {

    /**
     * 登录API
     * @param adminName
     * @param password
     * @return
     */
    public Admin findAdminByAdminNameAndPassword(String adminName, String password);
}
