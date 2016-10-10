package com.zlion.model;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zzs on 2016/9/26.
 */
public class BlockApplication {

    @Id
    private String id;

    private String geohash;

    private Date startDate;
    private Date endDate;

    private List<Long> uavs = new ArrayList<Long>();
    private Long applyUserId;

    private boolean confirm;
    private String msg;

    public BlockApplication() {
    }

    public BlockApplication(String geohash, Date startDate, Date endDate, List<Long> uavs, Long applyUserId, boolean confirm, String msg) {
        this.geohash = geohash;
        this.startDate = startDate;
        this.endDate = endDate;
        this.uavs = uavs;
        this.applyUserId = applyUserId;
        this.confirm = confirm;
        this.msg = msg;
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
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

    public List<Long> getUavs() {
        return uavs;
    }

    public void setUavs(List<Long> uavs) {
        this.uavs = uavs;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public void addUav(Long uav){
        uavs.add(uav);
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
