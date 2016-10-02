package com.zlion.repository;

import com.zlion.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by zzs on 2016/9/1.
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("select a from Admin a where a.username=:qUsername and a.password=:qPassword")
    Admin findByUsernameAndPwd(@Param("qUsername") String username,
                               @Param("qPassword") String password);

    Admin findByUsername(String username);

}
