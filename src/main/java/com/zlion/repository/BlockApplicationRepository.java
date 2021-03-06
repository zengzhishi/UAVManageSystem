package com.zlion.repository;

import com.zlion.model.BlockApplication;
import org.springframework.cglib.core.Block;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query(value = "{'geohash':?0,'endDate':{'$gte':?1},'startDate':{'$lte':?1}}")
    public List<BlockApplication> findByGeohashAndTime(String geohash, Date time);

    @Query(value = "{'geohash':?0 , 'confirm': true, $or: [{'endDate':{$gte:?1, $lte:?2}}, {'startDate':{$gte:?1, $lte:?2}}]}")
    public List<BlockApplication> getByGeohashAndTimeBetween(String geohash, Date beginTime, Date endTime);

    @Query(value = "{'geohash':?0 , 'confirm': true, $or: [{'endDate':{$gte:?1, $lte:?2}}, {'startDate':{$gte:?1, $lte:?2}}]}")
    public Page<BlockApplication> getByGeohashAndTimeBetween(String geohash, Date beginTime, Date endTime, Pageable pageable);

    public Page<BlockApplication> findByConfirm(boolean confirm, Pageable pageable);
}