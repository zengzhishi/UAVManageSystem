package com.zlion.repository;

import com.zlion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by zzs on 2016/9/1.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username=:qUsername")
    User findByUsername(@Param("qUsername") String username);

    @Query("select u from User u where u.id=:qUid")
    User findByUserId(@Param("qUid") Long id);

    @Query("select u from User u where u.username=:qUsername and u.password=:qPassword")
    User findByUsernameAndPwd(@Param("qUsername") String username, @Param("qPassword") String password);

    @Modifying
    @Query("update User u set u.password=:qPassword where u.id=:qId")
    int updateUserPwd(@Param("qId") Long id, @Param("qPassword") String password);

    @Modifying
    @Query("update User u set u.email=:qEmail, u.phone=:qPhone, u.address=:qAddress," +
            "u.groupName=:qGroupName where u.id=:qId")
    int updateDetail(@Param("qId") Long id, @Param("qEmail") String email,
                     @Param("qAddress") String address, @Param("qPhone") String phone,
                     @Param("qGroupName") String groupName);

    /*
    需要补完
     */
//    @Modifying
//    @Query("update User u set u.email=:qEmail where u.id=:qId")
//    int updateUserInfo(@Param("qId") Long id, @Param("qEmail") String email,
//                       @Param("qAddress") String address);
}
