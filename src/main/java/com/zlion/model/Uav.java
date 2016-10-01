package com.zlion.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zzs on 2016/9/2.
 */
@Entity
@Table(name = "uav")
public class Uav implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uuid;
    private Long user_id;
//    @Column(name = "groupName")
    private String groupName;
    private String info;
//    @Column(name = "registDate")
    private Date registDate;
    private int confirm;


    public Uav(){}

    public Uav(String uuid, Long user_id, String groupName, String info, Date registDate) {
        this.uuid = uuid;
        this.user_id = user_id;
        this.groupName = groupName;
        this.info = info;
        this.registDate = registDate;
        this.confirm = 0;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        return "Uav{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", user_id=" + user_id +
                ", groupName='" + groupName + '\'' +
                ", info='" + info + '\'' +
                ", registDate=" + registDate +
                '}';
    }
}
