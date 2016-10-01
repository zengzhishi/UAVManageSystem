package com.zlion.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zzs on 2016/9/1.
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

//    @Transient
//    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String phone;

    private String address;

    private String email;

//    @Column(name = "groupName")
    private String groupName;

//    @Column(name = "registDate")
    private Date registDate;

    private int confirm;


    public User() {
    }

    public User(String username, String password, String phone, String address,
                String email, String groupName, Date registDate) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.groupName = groupName;
        this.registDate = registDate;
        this.confirm = 0;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }

    public Date getRegistDate() {
        return registDate;
    }

    public void setRegistDate(Date registDate) {
        this.registDate = registDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


}
