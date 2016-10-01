package com.zlion.repository;

import com.zlion.model.Uav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//import com.sun.org.apache.xpath.internal.operations.String;

/**
 * Created by zzs on 2016/9/9.
 */
@Repository
public interface UavRepository extends JpaRepository<Uav, Long> {

    public Uav findByUuid(String uuid);

    @Query("select u from Uav u where u.user_id=:qUserId")
    public List<Uav>  findAllByUserId(@Param("qUserId") Long userId);

    /*
    修改确认信息为已确认
     */
    @Modifying
    @Query("update Uav u set u.confirm=1 where u.uuid=:qUuid")
    int confirmByUuid(@Param("qUuid") String uuid);

    @Modifying
    @Query("update Uav u set u.groupName=:qGroupName, u.info=:qInfo where u.uuid=:qUuid")
    int updateDataByUuid(@Param("qUuid") String uuid,
                         @Param("qGroupName") String groupName, @Param("qInfo") String info);


}
