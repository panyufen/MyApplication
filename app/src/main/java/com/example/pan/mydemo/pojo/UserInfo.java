package com.example.pan.mydemo.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by PAN on 2017/12/11.
 */

@Entity
public class UserInfo {
    @Id(autoincrement = true)
    @com.example.pan.mydemo.pojo.Id
    private Long id;
    private String name;
    private String age;
    private String mobile;
    private String address;
    private String mobile1;
    @Generated(hash = 417396528)
    public UserInfo(Long id, String name, String age, String mobile, String address,
            String mobile1) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.mobile = mobile;
        this.address = address;
        this.mobile1 = mobile1;
    }
    @Generated(hash = 1279772520)
    public UserInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAge() {
        return this.age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getMobile() {
        return this.mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getMobile1() {
        return this.mobile1;
    }
    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }
}
