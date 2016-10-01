package com.zlion.repository;

import com.zlion.model.BlockApplication;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by zzs on 2016/9/26.
 */
@Repository
public interface BlockApplicationRepository extends MongoRepository<BlockApplication, String> {

    /**
     * 按照当前时间和指定位置的geohash来查询
     * @param geohash
     * @param time
     */
    @Query(value = "{'geohash':?0,'endDate':{'$lt':?1},'startDate':{'$gt':?1}}")
    public BlockApplication qureyByGeohashAndTime(String geohash, Date time);

    public List<BlockApplication> qureyByGeohashAndTimeBetween(String geohash, Date beginTime, Date endTime);

}
