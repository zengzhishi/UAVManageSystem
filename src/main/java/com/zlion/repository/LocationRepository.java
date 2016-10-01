package com.zlion.repository;

import com.zlion.model.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by zzs on 2016/9/6.
 */
@Repository
public interface LocationRepository extends MongoRepository<Location, String> {

    public List<Location> findByUavId(Long uavId);

    /**
     * 根据 uavId字段和时间段进行查询--- 根据方法名的命名规范来进行方法定义查询
     * @param uavId
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<Location> findByUavIdAndTimeBetween(Long uavId, Date beginTime, Date endTime);





}
