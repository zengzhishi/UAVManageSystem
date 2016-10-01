package com.zlion.model;

import javax.persistence.Id;
import java.util.Date;

/**
 * Created by zzs on 2016/9/26.
 */
public class BlockApplication {

    @Id
    private String id;

    private String geohash;

    private Date startDate;
    private Date endDate;

    private Long [] uavIds;

    public BlockApplication() {
    }

    public BlockApplication(String id, String geohash, Date startDate, Date endDate, Long[] uavIds) {
        this.id = id;
        this.geohash = geohash;
        this.startDate = startDate;
        this.endDate = endDate;
        this.uavIds = uavIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long[] getUavIds() {
        return uavIds;
    }

    public void setUavIds(Long[] uavIds) {
        this.uavIds = uavIds;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }
}
